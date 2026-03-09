package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;

public enum StoreStatus {
    OPEN,
    CLOSED,
    DEFUNCT;

    public static StoreStatus from(String status) {
        if (status == null || status.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        try {
            return StoreStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_STORE_STATUS);
        }
    }
}
