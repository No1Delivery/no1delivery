package com.sparta.no1delivery.domain.review.application;

import com.sparta.no1delivery.domain.review.domain.Review;
import com.sparta.no1delivery.domain.review.domain.ReviewId;
import com.sparta.no1delivery.domain.review.domain.ReviewRepository;
import com.sparta.no1delivery.domain.review.domain.event.ReviewScoreChangedEvent;
import com.sparta.no1delivery.domain.review.domain.exception.ReviewNotFoundException;
import com.sparta.no1delivery.domain.review.domain.service.OrderInfoProvider;
import com.sparta.no1delivery.domain.review.domain.service.ReviewerCheck;
import com.sparta.no1delivery.domain.review.domain.service.StoreRatingCalculator;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.infrastructure.event.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewerCheck reviewerCheck;
    private final RoleCheck roleCheck;
    private final UserDetails userDetails;
    private final OrderInfoProvider orderInfoProvider;
    private final StoreRatingCalculator calculator;
    private final ReviewRepository reviewRepository;

    // 리뷰 작성
    @Transactional
    public UUID create(ReviewServiceDto.Create dto){

        Review review = Review.builder()
                .orderId(dto.getOrderId())
                .subject(dto.getSubject())
                .content(dto.getContent())
                .score(dto.getScore())
                .orderInfoProvider(orderInfoProvider)
                .roleCheck(roleCheck)
                .reviewerCheck(reviewerCheck)
                .userDetails(userDetails)
                .build();

        reviewRepository.save(review);

        // 이벤트 발행
        triggerEvent(review.getInfo().getStoreId());

        return review.getId().getId();
    }

    // 리뷰 수정
    @Transactional
    public void change(ReviewServiceDto.Change dto) {
        Review review = getReview(dto.getReviewId());
        review.change(dto.getSubject(), dto.getContent(), dto.getScore(), reviewerCheck, roleCheck);

        // 이벤트 발행
        triggerEvent(review.getInfo().getStoreId());
    }

    // 리뷰 삭제
    @Transactional
    public void remove(UUID reviewId) {
        Review review = getReview(reviewId);
        review.remove(reviewerCheck, roleCheck);

        // 이벤트 발행
        triggerEvent(review.getInfo().getStoreId());
    }

    private Review getReview(UUID reviewID) {
        return reviewRepository.findById(ReviewId.of(reviewID)).orElseThrow(ReviewNotFoundException::new);
    }

    // 매장 리뷰 평균 평점 업데이트(이벤트 발행)
    private void triggerEvent(UUID storeId) {
        // 평점 평균 구하기 전 리뷰 먼저 반영
        reviewRepository.flush();

        Events.trigger(new ReviewScoreChangedEvent(storeId, calculator.getReviewCount(storeId),
                calculator.getAverageRating(storeId)));
    }
}
