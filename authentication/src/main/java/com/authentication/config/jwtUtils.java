package com.authentication.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class jwtUtils {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    private static final SecretKey jwtSecretKey=Keys.hmacShaKeyFor("4324923894239048fksdnfksdnfksnd948320958092389402FNKLSJNFKSDKLN4r093285940823409583290583092".getBytes());


    public String createToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Add the "role" claim
        return generateToken(claims, username);
    }

    // Generate a JWT token
    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
