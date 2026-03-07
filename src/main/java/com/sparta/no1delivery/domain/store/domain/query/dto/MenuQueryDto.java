package com.sparta.no1delivery.domain.store.domain.query.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * 상품 조회는 매장에 속해있는 상품만 가능, SALE/SOLD_OUT 상태만 조회 가능
 * 페이징 없음
 * keyword는 상품명에서 키워드 검색
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuQueryDto {

    @Getter
    @Builder
    public static class Search {
        private String name;
        private List<UUID> menuId;
        private String keywords;
    }
}
