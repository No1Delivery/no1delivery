package com.sparta.no1delivery.domain.store.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메뉴 검색 요청 DTO")
public record MenuQueryRequestDto(

        @Schema(
                description = "검색할 메뉴 이름 또는 설명 키워드",
                example = "치즈 피자",
                nullable = true
        )
        String keyword
) {}