package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.global.domain.service.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserDetails implements UserDetails {
    @Override
    public Long getId() {
        return 1L;
    }

    @Override
    public String getName() {
        return "이름";
    }

    @Override
    public String getPhone() {
        return "010-1234-5678";
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}