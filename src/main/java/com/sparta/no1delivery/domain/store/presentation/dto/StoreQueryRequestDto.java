package com.sparta.no1delivery.domain.store.presentation.dto;

import com.sparta.no1delivery.domain.store.domain.StoreStatus;
import com.sparta.no1delivery.domain.store.domain.query.dto.StoreQueryDto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.UUID;

public record StoreQueryRequestDto(
        List<UUID> categoryIds,
        String storeStatus,
        String keyword,

        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
        Double latitude,

        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
        Double longitude,

        @Min(0) @Max(10)
        Double radiusKm,

        Boolean onlyMyStores
) {
    public StoreQueryRequestDto {
        if (onlyMyStores == null) onlyMyStores = false;
    }

    public StoreQueryDto.Search toSearchCondition() {
        return StoreQueryDto.Search.builder()
                .categoryIds(categoryIds)
                .storeStatus(storeStatus != null ? StoreStatus.valueOf(storeStatus) : null)
                .keyword(keyword)
                .latitude(latitude)
                .longitude(longitude)
                .radiusKm(radiusKm)
                .onlyMyStores(onlyMyStores)
                .build();
    }
}