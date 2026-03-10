package com.sparta.no1delivery.domain.user.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {

    @Getter
    @Builder
    public static class Token {
        private String token; // 인증 토큰
        private String refreshToken; // 리프레시 토큰
        private LocalDateTime tokenExpireTime; // 토큰 만료시간
        private LocalDateTime refreshTokenExpireTime; // 리프레시 토큰 만료시간
    }
}
