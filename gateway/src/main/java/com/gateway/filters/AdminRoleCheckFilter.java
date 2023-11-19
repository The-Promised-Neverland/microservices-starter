package com.gateway.filters;


import com.gateway.utils.jwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AdminRoleCheckFilter extends AbstractGatewayFilterFactory<AdminRoleCheckFilter.Config> {

    @Autowired
    private jwtUtil jwtUtil;

    public AdminRoleCheckFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("missing authorization header");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            try {
                jwtUtil.validateToken(authHeader);
            } catch (Exception e) {
                throw new RuntimeException("un authorized access to application");
            }
            String userEmail = jwtUtil.extractEmail(authHeader); // Replace with the actual method to extract the email
            String role = jwtUtil.extractRoleFromToken(authHeader);
            if (!"ADMIN".equals(role)) {
                throw new RuntimeException("You do not have admin access.");
            }
            // Add the email to the request headers
            exchange.getRequest().mutate().header("user", userEmail);
            exchange.getRequest().mutate().header("role", role);
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}