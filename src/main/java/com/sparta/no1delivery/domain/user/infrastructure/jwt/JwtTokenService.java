package com.sparta.no1delivery.domain.user.infrastructure.jwt;

import com.sparta.no1delivery.domain.user.application.TokenService;
import com.sparta.no1delivery.domain.user.application.dto.TokenDto;
import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.repository.UserRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTokenService implements TokenService {

    private final UserRepository userRepository;
    private final JwtProperties properties;
    private final Key key;
    private final long validTime;

    public JwtTokenService(JwtProperties properties, UserRepository userRepository) {
        this.properties = properties;
        this.userRepository = userRepository;

        // secret -> Base64 디코딩
        byte[] secret = Decoders.BASE64.decode(properties.secret());
        this.key = Keys.hmacShaKeyFor(secret);

        this.validTime = properties.validTime() * 1000L; // 초 -> 밀리초
    }

    @Override
    public TokenDto.Token create(String loginId) {
        User user = getUser(loginId);

        Date today = new Date();
        Date expiredDate = new Date(today.getTime() + validTime);
        String token =  Jwts.builder()
                .setSubject(user.getUserId().toString())
                .claim("ROLE", user.getRole())
                .claim("NICKNAME", user.getNickname())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiredDate)
                .compact();

        LocalDateTime exired = LocalDateTime.ofInstant(expiredDate.toInstant(), ZoneId.of("Asia/Seoul"));
        return TokenDto.Token.builder()
                .token(token)
                .tokenExpireTime(exired)
                .build();
    }

    @Override
    public TokenDto.Token refresh(TokenDto.Refresh dto) {
        return null;
    }

    @Override
    public void invalidate(TokenDto.InValidate dto) {

    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
