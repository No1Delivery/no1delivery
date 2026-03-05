package com.sparta.no1delivery.domain.order.domain.query;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

// 주문 조회 전용 DTO
//목록 조회 및 조건 기반 조회 시 사용(검색)
public class OrderQueryDto {

    @Getter
    @Builder
    public static class Search {
        private List<UUID> orderIds;
        private Long ordererId;
        private List<UUID> storeIds;
        private List<String> statuses;
    }
}