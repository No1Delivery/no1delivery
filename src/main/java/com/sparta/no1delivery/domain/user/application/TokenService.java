package com.sparta.no1delivery.domain.user.application;

import com.sparta.no1delivery.domain.user.application.dto.TokenDto;

import java.util.UUID;

public interface TokenService {

    TokenDto.Token create(String loginId); // 토큰 생성

    TokenDto.Token refresh(TokenDto.Refresh dto); // 토큰 갱신

    void invalidate(TokenDto.InValidate dto); // 토큰 접근 차// 단

    void validate(String token); // 토큰 유효성 검사

    Long getUserId(String token);
}