package com.authentication.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class jwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity}")
    private long jwtTokenValidity;

    private SecretKey jwtSecretKey;
    @PostConstruct
    public void jwtUtils() {
        jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

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
        long tokenValidity = (validityPeriod != null) ? validityPeriod : jwtTokenValidity;
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

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims=extractClaimsFromJwt(token);
        return claims.getExpiration();
    }

    public String getRoleFromToken(String token){
        Claims claims=extractClaimsFromJwt(token);
        return claims.get("Role").toString();
    }

    public Object getOTPRequestIdFromToken(String token){
        Claims claims=extractClaimsFromJwt(token);
        return claims.get("OTP_RequestID");
    }
}
