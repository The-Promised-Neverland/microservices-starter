package com.authentication.controller;

import com.authentication.module.AuthRequest;
import com.authentication.module.UserCredentials;
import com.authentication.service.authService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/authenticate")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest, HttpServletResponse response){
        String preAuthJwt=authService.authenticate(authRequest.getEmail(), authRequest.getPassword());

        Cookie cookie = new Cookie("jwt", preAuthJwt); // Create a new cookie
        cookie.setHttpOnly(true); // Set the HTTP-only flag for security
        cookie.setMaxAge(300); // Set the expiration time for the cookie in seconds (adjust as needed)
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>("User verified successful. Proceed for OTP validation",HttpStatus.OK);
    }


    @PostMapping("/verifyOTP")
    public ResponseEntity<String> getToken(@RequestParam("X-OTP") String otp, HttpServletRequest request, HttpServletResponse response){
        String jwt=authService.verifyOtp(otp);

        Cookie cookie = new Cookie("jwt", jwt); // Create a new cookie
        cookie.setHttpOnly(true); // Set the HTTP-only flag for security
        cookie.setMaxAge(3600 * 5); // Set the expiration time for the cookie in seconds (adjust as needed)
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>("OTP validation Successful.",HttpStatus.ACCEPTED);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("HELLO",HttpStatus.OK);
    }

    @GetMapping("/helloNeedsRole")
    public ResponseEntity<String> test2(){
        return new ResponseEntity<>("HELLO, authenticated now fuklky",HttpStatus.OK);
    }
}
