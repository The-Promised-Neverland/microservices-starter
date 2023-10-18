package com.authentication.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class extractRoleUtils {

    public String getRoleAsString(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.startsWith("ROLE_")) {
                return role.substring("ROLE_".length());
            }
        }
        // Return a default role or handle the case where no role is found
        return "DEFAULT_ROLE";
    }
}
