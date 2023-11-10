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
        user.setRole("user");
        credentialRepository.save(user);
    }

    public String authenticate(String email, String password){
        UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(email,password);
        Authentication authenticate=authenticationManager.authenticate(authtoken);
        if(authenticate.isAuthenticated()) {
            String phoneNumber=((UserMapper_Security) authenticate.getPrincipal()).getPhoneNumber();
//            String requestID=otpSender.sendOtpSms(phoneNumber);
            String requestID="123123412";
            Map<String,Object> claims=new HashMap<>();
            claims.put("OTP_RequestID",requestID);
            claims.put("Roles","PENDING_AUTH_USER"); // OTP REQUIRED
            logger.info("username: " + ((UserMapper_Security) authenticate.getPrincipal()).getUsername());
            logger.info("RequestID: " + requestID);
            return jwtUtils.generateToken(claims,((UserMapper_Security) authenticate.getPrincipal()).getUsername(), 300000L);
        }
        throw new RuntimeException("Invalid user");
    }

    public String verifyOtp(String OTP){
        Authentication clearedOTPUser=SecurityContextHolder.getContext().getAuthentication();
        logger.info("clearedOTPUser.getName(): " + clearedOTPUser.getName());
        UserDetails userDetails = userService.loadUserByUsername(clearedOTPUser.getName());
        UsernamePasswordAuthenticationToken finalAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(finalAuth);

//        String token = jwtUtils.generateToken(clearedOTPUser.getName(),extractRole.getRoleAsString(finalAuth.getAuthorities()));
        return "lol";
    }
}
