package com.authentication.controller;

import com.authentication.module.AuthRequest;
import com.authentication.module.UserCredentials;
import com.authentication.service.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class authController {
    @Autowired
    private authService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserCredentials user){
        authService.saveUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest){
        String token=authService.generateToken(authRequest.getEmail(), authRequest.getPassword());
        return new ResponseEntity<>(token,HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam(value = "token") String token){
        authService.validateToken(token);
        return new ResponseEntity<>("Token validated",HttpStatus.OK);
    }
}
