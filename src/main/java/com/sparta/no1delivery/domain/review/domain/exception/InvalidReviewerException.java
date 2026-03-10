package com.sparta.no1delivery.domain.review.domain.exception;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;

public class InvalidReviewerException extends CustomException {
    public InvalidReviewerException() {
        super(ErrorCode.INVALID_REVIEWER_NOT_FOUND);
    }
}
