package com.sparta.no1delivery.domain.review.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

public class ReviewServiceDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Review {
        private UUID id; // 리뷰 ID
        private String content; // 리뷰 내용
    }
}
