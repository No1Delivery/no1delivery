package com.sparta.no1delivery.domain.user.infrastructure.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        long validTime // 토큰 유효시간
) {}
