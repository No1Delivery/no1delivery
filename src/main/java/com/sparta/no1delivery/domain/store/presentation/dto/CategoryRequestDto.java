package com.sparta.no1delivery.domain.store.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryRequestDto {

    @Data
    public static class Update {

        @NotEmpty
        private List<UUID> categoryIds;
    }
}
