package com.authentication.utils;

import com.authentication.module.UserMapper_Security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class jwtUtils {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    private static final SecretKey jwtSecretKey=Keys.hmacShaKeyFor("4324923894239048fksdnfksdnfksnd948320958092389402FNKLSJNFKSDKLN4r093285940823409583290583092".getBytes());


    public String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Claims extractClaimsFromJwt(String jwt) {
        return Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(jwt).getBody();
    }

    // Generate a JWT token
    public String generateToken(Map<String, Object> claims, String subject, Long validityPeriod) {
        long tokenValidity = (validityPeriod != null) ? validityPeriod : JWT_TOKEN_VALIDITY;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String getEmailFromToken(String token) {
        Claims claims=extractClaimsFromJwt(token);
        return claims.getSubject();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims=extractClaimsFromJwt(token);
        return claims.getExpiration();
    }

    public String getRoleFromToken(String token){
        Claims claims=extractClaimsFromJwt(token);
        return claims.get("Roles").toString();
    }

    public String getOTPRequestIdFromToken(String token){
        Claims claims=extractClaimsFromJwt(token);
        return claims.get("OTP_RequestID").toString();
    }
    public Boolean validateToken(String token, UserMapper_Security userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
