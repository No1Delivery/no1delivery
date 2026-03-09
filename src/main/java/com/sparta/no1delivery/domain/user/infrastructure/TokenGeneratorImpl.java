package com.sparta.no1delivery.domain.user.infrastructure;

import com.sparta.no1delivery.domain.user.application.TokenService;
import com.sparta.no1delivery.domain.user.application.dto.TokenDto;
import com.sparta.no1delivery.domain.user.domain.service.TokenGenerator;
import com.sparta.no1delivery.domain.user.domain.vo.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenGeneratorImpl implements TokenGenerator {
    private final TokenService tokenService;

    @Override
    public Token generate(String loginId) {
        TokenDto.Token token = tokenService.create(loginId);

        return Token.builder()
                .token(token.getToken())
                .refreshToken(token.getRefreshToken())
                .tokenExpireTime(token.getTokenExpireTime())
                .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
                .build();
    }
}
