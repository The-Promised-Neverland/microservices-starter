package com.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class jwtUtil {
    private static final SecretKey jwtSecretKey= Keys.hmacShaKeyFor("4324923894239048fksdnfksdnfksnd948320958092389402FNKLSJNFKSDKLN4r093285940823409583290583092".getBytes());

    // Validate token
    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
    }

    public String extractRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract the "role" claim from JWT
            String role = (String) claims.get("role");

            return role;
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey) // Replace with your JWT secret key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("sub", String.class); // Assumes "sub" is a string, adjust the type accordingly
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract 'sub' claim from JWT");
        }
    }
}
