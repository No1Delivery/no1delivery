package com.sparta.no1delivery.global.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtFilter jwtFilter;
/**

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(c -> c.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint((req, res, e) -> {
                        // 미로그인 상태에서 인가가 되지 않은 자원을 접근할때(401)
                        res.sendError(HttpStatus.UNAUTHORIZED.value(), "회원 인증이 필요합니다.");
                        log.error("AuthenticationException: {}", e.getMessage(), e);
                    });

                    c.accessDeniedHandler((req, res, e) -> {
                        // 로그인 상태이지만 권한이 없는 자원을 접근할때 (403) - Forbidden
                        res.sendError(HttpStatus.FORBIDDEN.value(), "접근할 수 없는 리소스입니다.");
                        log.error("AccessDeniedException: {}", e.getMessage(), e);
                    });
                })
                .authorizeHttpRequests(c ->
                        c.requestMatchers("/v1/auth/**").permitAll().requestMatchers("/v3/api-docs/**", "/api-docs/**", "/api-docs.html", "/swagger-ui/**").permitAll()
                                .anyRequest().authenticated());

        return http.build();
    }
    @Bean // 사용할 해시 암호화 방식(BCrypt)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
