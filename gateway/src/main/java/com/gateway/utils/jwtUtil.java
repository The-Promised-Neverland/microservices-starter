package com.gateway.utils;

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
}
