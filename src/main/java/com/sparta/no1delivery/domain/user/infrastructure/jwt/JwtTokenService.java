package com.sparta.no1delivery.domain.user.infrastructure.jwt;

import com.sparta.no1delivery.domain.user.application.TokenService;
import com.sparta.no1delivery.domain.user.application.dto.TokenDto;
import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.repository.UserRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.UnsupportedKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
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

    /**
     * 토큰 유효성 검사
     * @param token
     */
    @Override
    public void validate(String token) {
        String message = null;
        Exception error = null;
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getPayload();
        } catch (SecurityException | MalformedJwtException e) {
            message = "잘못된 형식의 JWT 서명";
            error = e;
        } catch (ExpiredJwtException e) {
            message = "만료된 JWT 토큰";
            error = e;
        } catch (UnsupportedJwtException e) {
            message = "지원하지 않는 JWT 토큰";
            error = e;
        } catch (Exception e) {
            message = "JWT 토큰을 처리하는데 문제가 발생";
            error = e;
        }

        if (StringUtils.hasText(message)) {
            log.error("JWT 토큰 검증 실패, 사유: {}", message, error);
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
