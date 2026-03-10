package com.sparta.no1delivery.domain.store.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "가게 카테고리 관련 요청 DTO")
public class CategoryRequestDto {

    @Data
    @Schema(description = "가게 카테고리 수정 요청 정보")
    public static class Update {

        @NotEmpty(message = "카테고리 ID는 최소 하나 이상이어야 합니다.")
        @Schema(
                description = "수정할 카테고리 ID 리스트",
                example = "[\"550e8400-e29b-41d4-a716-446655440000\", \"678e8400-e29b-41d4-a716-446655440001\"]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        private List<UUID> categoryIds;
    }
}