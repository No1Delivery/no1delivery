package com.sparta.no1delivery.domain.category.presentation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryResponseDto {

    private List<UUID> categoryIds;

    @Getter
    @Builder
    public static class Detail{
        private UUID id;
        private String name;
    }

    public static CategoryResponseDto of(List<UUID>  ids) {
        return CategoryResponseDto.builder()
                .categoryIds(ids)
                .build();
    }
}
