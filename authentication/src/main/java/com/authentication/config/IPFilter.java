package com.authentication.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IPFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(IPFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        // Log the IP address here
        logger.info("IP address: " + ipAddress);

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null){
            logger.info("auth: " + authentication.toString());
        } else {
            logger.info("auth: " + null);
        }


        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
