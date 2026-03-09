package com.sparta.no1delivery.domain.user.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDto {

    @Getter
    @Builder
    @ToString
    public static class Token {
        private String token; // 인증 토큰
        private String refreshToken; // 리프레시 토큰
        private LocalDateTime tokenExpireTime; // 토큰 만료시간
        private LocalDateTime refreshTokenExpireTime; // 리프레시 토큰 만료시간
    }

    @Getter
    @Builder
    @ToString
    public static class Refresh {

    }

    @Getter
    @Builder
    @ToString
    public static class InValidate {

    }
}
