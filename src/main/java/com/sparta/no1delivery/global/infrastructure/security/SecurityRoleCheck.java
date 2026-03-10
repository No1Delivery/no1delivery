package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.global.domain.RoleCheck;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SecurityRoleCheck implements RoleCheck {

    @Override
    public boolean hasRole(String role) {
        return getAuthorities().stream().anyMatch(r -> r.equals(ensureRolePrefix(role)));
    }

    @Override
    public boolean hasRole(List<String> roles) {
        Collection<String> userAuthorities = getAuthorities();
        return roles.stream()
                .map(this::ensureRolePrefix)
                .anyMatch(r -> userAuthorities.stream().anyMatch(role -> role.equals(r)));
    }

    private Collection<String> getAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            return List.of();
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    private String ensureRolePrefix(String role) {
        if (role == null) return "";
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }
}
