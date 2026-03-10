package com.sparta.no1delivery.global.infrastructure.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .displayName("사용자 API")
                .pathsToMatch("/v1/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi storeApi() {
        return GroupedOpenApi.builder()
                .group("stores")
                .displayName("가게 API")
                .pathsToMatch("/v1/stores/**")
                .build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("categories")
                .displayName("카테고리 API")
                .pathsToMatch("/v1/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("orders")
                .displayName("주문 API")
                .pathsToMatch("/v1/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi paymentApi() {
        return GroupedOpenApi.builder()
                .group("payments")
                .displayName("결제 API")
                .pathsToMatch("/v1/payments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reviewApi() {
        return GroupedOpenApi.builder()
                .group("reviews")
                .displayName("리뷰 API")
                .pathsToMatch("/v1/reviews/**")
                .build();
    }

    @Bean
    public GroupedOpenApi aiApi() {
        return GroupedOpenApi.builder()
                .group("ai")
                .displayName("AI 요청 API")
                .pathsToMatch("/v1/ai/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {

        // API 문서 정보 설정
        Info info = new Info()
                .title("No. 1 Delivery API")
                .description("배달 서비스 No. 1 Delivery의  API 명세서입니다.")
                .version("1.0.0");

        // Security Scheme 정의 (JWT 토큰 기반 인증)
        SecurityScheme jwtScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)  // HTTP 기반 인증
                .scheme("bearer")  // Bearer 방식 사용
                .bearerFormat("JWT");  // JWT 사용 명시

        return new OpenAPI()
                .addServersItem(new Server().url("/"))  // 기본 서버 설정
                .info(info)  // API 문서 정보 추가
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))  // JWT 인증 적용
                .components(new Components().addSecuritySchemes("BearerAuth", jwtScheme)); // 보안 스키마 설정
    }
}