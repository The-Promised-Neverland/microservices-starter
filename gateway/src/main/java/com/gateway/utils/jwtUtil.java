package com.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;



@Component
public class jwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity}")
    private long jwtTokenValidity;

    private SecretKey jwtSecretKey;
    @PostConstruct
    public void jwtUtils() {
        jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

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
            String role = (String) claims.get("Role");
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
