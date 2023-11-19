package com.reviews.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CredentialFilter extends OncePerRequestFilter {
    Logger logger= LoggerFactory.getLogger(CredentialFilter.class);

    private static final List<String> EXCLUDED_URLS = List.of("/api/reviews/product/.*");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userEmail = request.getHeader("user");
        String role = request.getHeader("role");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
logger.info("email: " + userEmail + " , role: " + role );
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userEmail,null,authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUri = request.getRequestURI();
        return EXCLUDED_URLS.stream().anyMatch(url -> requestUri.matches(url));
    }
}
