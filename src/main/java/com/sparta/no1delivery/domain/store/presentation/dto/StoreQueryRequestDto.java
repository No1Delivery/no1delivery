package com.sparta.no1delivery.domain.store.presentation.dto;

import com.sparta.no1delivery.domain.store.domain.StoreStatus;
import com.sparta.no1delivery.domain.store.domain.query.dto.StoreQueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.UUID;

@Schema(description = "가게 목록 검색 및 필터링 요청 DTO")
public record StoreQueryRequestDto(
        @Schema(description = "필터링할 카테고리 ID 목록", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]")
        List<UUID> categoryIds,

        @Schema(description = "가게 상태 (OPEN, CLOSED, SHUTDOWN 등)", example = "OPEN")
        String storeStatus,

        @Schema(description = "가게 이름 검색 키워드", example = "치킨")
        String keyword,

        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
        @Schema(description = "중심점 위도 (-90.0 ~ 90.0)", example = "37.5665")
        Double latitude,

        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
        @Schema(description = "중심점 경도 (-180.0 ~ 180.0)", example = "126.9780")
        Double longitude,

        @Min(0) @Max(10)
        @Schema(description = "검색 반경 (단위: km, 최대 10km)", example = "3.0")
        Double radiusKm,

        @Schema(description = "내 가게만 보기 여부 (관리자/사장님 전용)", defaultValue = "false")
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