package com.authentication.service;

import com.authentication.config.VonageOTPSender;
import com.authentication.config.VonageOTPVerifier;
import com.authentication.config.jwtUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
    private jwtUtils jwt;

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
            logger.info("PHONE NUMBER OF USER: " + phoneNumber);
            String requestID=otpSender.sendOtpSms(phoneNumber);
            return requestID;
        }
        throw new RuntimeException("Invalid user");
    }

    public String verifyOtp(String OTP, String requestID){
        boolean isOTPValid = otpVerifier.verifyOtp(requestID,OTP);
        if(!isOTPValid){
            throw new RuntimeException("OTP timeout or does not match");
        }
        Authentication currentLogged=SecurityContextHolder.getContext().getAuthentication();
        String token = jwt.createToken(currentLogged.getPrincipal().toString(),extractRole.getRoleAsString(currentLogged.getAuthorities()));
        return token;
    }
}
