package com.sparta.no1delivery.domain.review.domain.exception;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;

import java.util.UUID;

public class ReviewAuthorityException extends CustomException {
    public ReviewAuthorityException() {

        super(ErrorCode.INVALID_REVIEW_UNAUTHORIZED);
    }

    // 좀 더 상세한 메세지가 필요한 경우
    public ReviewAuthorityException(UUID orderId) {

        super(ErrorCode.INVALID_REVIEW_DETAIL_UNAUTHORIZED);
    }
}
