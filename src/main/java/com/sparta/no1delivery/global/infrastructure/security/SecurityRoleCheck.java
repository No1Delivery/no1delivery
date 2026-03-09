package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.global.domain.RoleCheck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityRoleCheck implements RoleCheck {

    @Override
    public boolean hasRole(String role) {
        return false;
    }

    @Override
    public boolean hasRole(List<String> roles) {
        return false;
    }
}
