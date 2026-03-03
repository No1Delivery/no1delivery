package com.sparta.no1delivery.domain.store.domain;

public enum MenuStatus {
    SALE,       // 판매 중
    SOLD_OUT,   // 품절 (메뉴 조회 시 보임)
    HIDDEN      // 숨김 (메뉴 조회 시 안보임)
}
