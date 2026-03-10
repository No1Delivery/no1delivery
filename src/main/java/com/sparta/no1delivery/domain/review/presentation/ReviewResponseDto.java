package com.sparta.no1delivery.domain.review.presentation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import com.sparta.no1delivery.domain.review.application.ReviewServiceDto.ReviewDto;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "리뷰 상세 응답")
public class ReviewResponseDto {

    @Schema(description = "리뷰 ID")
    private UUID reviewId;

    @Schema(description = "주문 ID")
    private UUID orderId;

    @Schema(description = "상점 ID")
    private UUID storeId;

    @Schema(description = "상점명")
    private String storeName;

    @Schema(description = "리뷰 내용")
    private String comment;

    @Schema(description = "평점")
    private int rating;

    @Schema(description = "작성자 닉네임")
    private String reviewerName;

    @Schema(description = "작성 일시")
    private LocalDateTime createdAt;

    public static ReviewResponseDto from(ReviewDto dto) {
        return ReviewResponseDto.builder()
                .reviewId(dto.getReviewId())
                .orderId(dto.getOrderId())
                .storeId(dto.getStoreId())
                .storeName(dto.getStoreName())
                .comment(dto.getComment())
                .rating(dto.getRating())
                .reviewerName(dto.getReviewerName())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
