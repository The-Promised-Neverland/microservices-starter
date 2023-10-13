package com.authentication.service;

import com.authentication.config.jwtUtils;
import com.authentication.module.UserCredentials;
import com.authentication.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class authService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtUtils jwt;

    @Autowired
    AuthenticationManager authenticationManager;

    public void saveUser(UserCredentials user){
        UserCredentials existingUser=credentialRepository.findByEmail(user.getEmail());
        if(existingUser!=null){
            throw new RuntimeException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        credentialRepository.save(user);
    }

    public String generateToken(String email, String password){
        UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(email,password);
        Authentication authenticate=authenticationManager.authenticate(authtoken);
        if(authenticate.isAuthenticated()) {
            return jwt.createToken(email);
        }
        throw new RuntimeException("Invalid user");
    }

    public void validateToken(String token){
        jwt.validateToken(token);
    }
}
