package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;

public enum MenuStatus {
    SALE,       // 판매 중
    SOLD_OUT,   // 품절 (메뉴 조회 시 보임)
    HIDDEN;      // 숨김 (메뉴 조회 시 안보임)

    public static MenuStatus from(String status) {
        if (status == null || status.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        try {
            return MenuStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_STORE_STATUS);
        }
    }
}
