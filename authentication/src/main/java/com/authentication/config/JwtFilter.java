package com.authentication.config;

import com.authentication.utils.jwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private jwtUtils jwtUtils;

    public JwtFilter(jwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    private static final List<String> EXCLUDED_URLS = List.of("/api/auth/register", "/api/auth/authenticate");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtUtils.extractJwtFromRequest(request);

        String email=jwtUtils.getEmailFromToken(jwt);

        String role=jwtUtils.getRoleFromToken(jwt);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        Object OtpRequestId=jwtUtils.getOTPRequestIdFromToken(jwt);

        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(email,null,authorities);
        token.setDetails(OtpRequestId);

        if(jwtUtils.isTokenExpired(jwt)==true){
            throw new RuntimeException("JWT token has expired");
        }
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUri = request.getRequestURI();
        return EXCLUDED_URLS.stream().anyMatch(url -> requestUri.matches(url));
    }
}
