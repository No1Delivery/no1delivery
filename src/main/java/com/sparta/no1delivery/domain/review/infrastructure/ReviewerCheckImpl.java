package com.sparta.no1delivery.domain.review.infrastructure;

import com.sparta.no1delivery.domain.order.domain.query.OrderQueryRepository;
import com.sparta.no1delivery.domain.review.domain.ReviewId;
import com.sparta.no1delivery.domain.review.domain.ReviewRepository;
import com.sparta.no1delivery.domain.review.domain.exception.ReviewNotFoundException;
import com.sparta.no1delivery.domain.review.domain.service.OrderInfoProvider;
import com.sparta.no1delivery.domain.review.domain.service.ReviewerCheck;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewerCheckImpl implements ReviewerCheck {
    private final UserDetails userDetails;
    private final ReviewRepository reviewRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderInfoProvider orderInfoProvider;

    /**
     * 1. 리뷰 작성 시 주문자와 로그인 한 사용자가 같은지 체크
     * 2. 리뷰 수정인 경우 리뷰 작성자가 로그인한 사용자와 같은지도 체크
     * ( 주문 번호는 최초 등록시에만 수정이 되므로 수정일 땐 체크 불필요)
     */
    @Override
    public boolean check(ReviewId reviewId, UUID orderId) {
        UUID currentUserId = userDetails.getId();
        return reviewId == null ? isOrderer(orderId, currentUserId) : isReviewer(reviewId, currentUserId);
    }

    private boolean isOrderer(UUID orderId, UUID userId) {
        return orderQueryRepository.findById(orderId)
                .map(order -> order.getOrderer().getUserId().equals(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private boolean isReviewer(ReviewId reviewId, UUID userId) {
        return reviewRepository.findById(reviewId)
                .map(review -> review.getReviewer().getId().equals(userId))
                .orElseThrow(ReviewNotFoundException::new);
    }
}
