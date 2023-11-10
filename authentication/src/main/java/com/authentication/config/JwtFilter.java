package com.authentication.config;

import com.authentication.utils.jwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private jwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtUtils.extractJwtFromRequest(request);

        String email=jwtUtils.getEmailFromToken(jwt);
        String role=jwtUtils.getRoleFromToken(jwt);
        String OtpRequestId=jwtUtils.getOTPRequestIdFromToken(jwt);
    }
}
