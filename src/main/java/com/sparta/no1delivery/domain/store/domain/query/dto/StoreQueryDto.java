package com.sparta.no1delivery.domain.store.domain.query.dto;

import com.sparta.no1delivery.domain.store.domain.StoreStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * CUSTOMER는 기본배송지 반경 3키로 내 OPEN/CLOSED 상태의 가게만 조회 가능
 * OWNER는 자신이 소유한 모든 가게 조회 가능
 * MANAGER, MASTER는 모든 가게 조회 가능
 * keyword는 가게 이름/메뉴 이름 중에 포함되었는지 체크
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreQueryDto {

    @Getter
    @Builder
    public static class Search {
        private List<UUID> categoryIds;
        private String storeName;
        private StoreStatus storeStatus;
        private String keyword;
        private Double latitude;
        private Double longitude;
        private Double radiusKm;
        private Boolean onlyMyStores; // 가게 주인용: 자신이 소유한 가게만 조회하기
    }
}
