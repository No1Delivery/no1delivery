package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.global.domain.service.UserDetails; // 내가 만든 인터페이스
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUserDetails implements UserDetails {

    @Override
    public Long getId() {
        return getPrincipal()
                .map(UserDetailsImpl::getUserId)
                .orElse(null);
    }

    @Override
    public String getName() {
        return getPrincipal()
                .map(UserDetailsImpl::getName)
                .orElse(null);
    }

    @Override
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    private Optional<UserDetailsImpl> getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl principal) {
            return Optional.of(principal);
        }
        return Optional.empty();
    }
}