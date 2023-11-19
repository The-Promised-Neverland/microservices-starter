package com.authentication.service;

import com.authentication.config.VonageOTPSender;
import com.authentication.config.VonageOTPVerifier;
import com.authentication.utils.jwtUtils;
import com.authentication.module.UserCredentials;
import com.authentication.module.UserMapper_Security;
import com.authentication.repository.CredentialRepository;
import com.authentication.utils.extractRoleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class authService {

    @Autowired
    private VonageOTPSender otpSender;

    @Autowired
    private VonageOTPVerifier otpVerifier;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private extractRoleUtils extractRole;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtUtils jwtUtils;

    @Autowired
    private userService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    Logger logger= LoggerFactory.getLogger(authService.class);

    public void saveUser(UserCredentials user){
        UserCredentials existingUser=credentialRepository.findByEmail(user.getEmail());
        if(existingUser!=null){
            throw new RuntimeException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        credentialRepository.save(user);
    }

    public String authenticate(String email, String password){
        UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(email,password);
        Authentication authenticate=authenticationManager.authenticate(authtoken);
        if(authenticate.isAuthenticated()) {
            String phoneNumber=((UserMapper_Security) authenticate.getPrincipal()).getPhoneNumber();
            String requestID=otpSender.sendOtpSms(phoneNumber);

            Map<String,Object> claims=new HashMap<>();
            claims.put("OTP_RequestID",requestID);
            claims.put("Role","PENDING_AUTH_USER"); // OTP REQUIRED

            return jwtUtils.generateToken(claims,((UserMapper_Security) authenticate.getPrincipal()).getUsername(), 300000L);
        }
        throw new RuntimeException("Invalid user");
    }

    public String verifyOtp(String OTP){
        Authentication clearedOTPUser=SecurityContextHolder.getContext().getAuthentication();

        if(otpVerifier.verifyOtp(clearedOTPUser.getDetails().toString(),OTP)==false){
            throw new RuntimeException("OTP expired or incorrect.");
        }

        UserDetails userDetails = userService.loadUserByUsername(clearedOTPUser.getName());
        Map<String,Object> claims=new HashMap<>();
        claims.put("Role", extractRole.getRoleAsString(userDetails.getAuthorities())); // OTP REQUIRED
        String token = jwtUtils.generateToken(claims,userDetails.getUsername(),null);
        return token;
    }
}
