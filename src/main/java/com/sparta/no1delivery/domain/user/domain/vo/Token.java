package com.sparta.no1delivery.domain.user.domain.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Token(
    String token,
    String refreshToken,
    LocalDateTime tokenExpireTime,
    LocalDateTime refreshTokenExpireTime
){}
