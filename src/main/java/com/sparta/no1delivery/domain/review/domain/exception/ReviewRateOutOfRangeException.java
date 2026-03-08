package com.sparta.no1delivery.domain.review.domain.exception;

import org.apache.coyote.BadRequestException;

public class ReviewRateOutOfRangeException extends BadRequestException {
    public ReviewRateOutOfRangeException(int rating) {
        super("리뷰 평점은 1점에서 5점 사이여야 합니다. [입력 된 평점: %d".formatted(rating));
    }
}
