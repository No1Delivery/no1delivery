package com.sparta.no1delivery.domain.user.application;

import com.sparta.no1delivery.domain.user.application.dto.TokenDto;

public interface TokenService {

    TokenDto.Token create(String loginId); // 토큰 생성

    TokenDto.Token refresh(TokenDto.Refresh dto); // 토큰 갱신

    void invalidate(TokenDto.InValidate dto); // 토큰 접근 차단
}
