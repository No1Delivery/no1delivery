package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.domain.user.application.TokenService;
import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.repository.UserRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 요청 헤더 : Authorization: Bearer 토큰
        String token = getToken((HttpServletRequest) request);
        if (StringUtils.hasText(token)) {
            try {
                tokenService.validate(token); // 토큰 유효성 검사

                // 로그인 처리
                Long userId = tokenService.getUserId(token);
                User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                UserDetails userDetails = new UserDetailsImpl(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication); // 로그인 처리

            } catch (CustomException e) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendError(e.getStatusCode().value(), e.getMessage());
            }
        }

        chain.doFilter(request, response); // 다음 필터 체인으로 넘어간다.
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.toUpperCase().startsWith("BEARER ")) {
            return token.trim().substring(7);
        }

        return null;
    }
}
