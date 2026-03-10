package com.sparta.no1delivery.domain.review.domain.exception;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;

import java.util.UUID;

public class InvalidOrderStateForReviewException extends CustomException {
    public InvalidOrderStateForReviewException(UUID orderId) {

        super(ErrorCode.INVALID_REVIEW_STATE);
    }
}
