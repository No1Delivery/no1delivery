package com.sparta.no1delivery.domain.order.domain.query;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

// 주문 조회 전용 DTO

public class OrderQueryDto {
    @Getter
    @Builder
    public static class Search {

        // 특정 주문 조회
        private List<UUID> orderIds;

        // 주문자 이름 검색
        private String ordererName;

        // 매장 검색
        private List<UUID> storeIds;
        private String storeName;

        // 배송 주소 검색
        private String deliveryAddress;

        // 주문 상태 필터
        private List<String> orderStatuses;

    }
}