package com.sparta.no1delivery.domain.review.application;

import com.sparta.no1delivery.domain.review.domain.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewServiceDto {

    @Getter
    @Builder
    public static class Create {
        private UUID orderId;
        private String comment;
        private int rating;
    }

    @Getter
    @Builder
    public static class Change {
        private UUID reviewId;
        private String comment;
        private int rating;
    }

    @Getter
    @Builder
    public static class ReviewDto {
        private UUID reviewId;
        private UUID orderId;
        private UUID storeId;
        private String storeName;
        private String comment;
        private int rating;
        private String reviewerName;
        private LocalDateTime createdAt;

        public static ReviewDto from(Review review) {
            return ReviewDto.builder()
                    .reviewId(review.getId().getId())
                    .orderId(review.getInfo().getOrderId())
                    .storeId(review.getInfo().getStoreId())
                    .storeName(review.getInfo().getStoreName())
                    .comment(review.getContent().getComment())
                    .rating(review.getContent().getScore())
                    .reviewerName(review.getReviewer().getReviewerName())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }
}
