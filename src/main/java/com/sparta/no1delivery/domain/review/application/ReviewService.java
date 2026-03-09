package com.sparta.no1delivery.domain.review.application;

import com.sparta.no1delivery.domain.review.domain.Review;
import com.sparta.no1delivery.domain.review.domain.ReviewRepository;
import com.sparta.no1delivery.domain.review.domain.service.OrderInfoProvider;
import com.sparta.no1delivery.domain.review.domain.service.ReviewerCheck;
import com.sparta.no1delivery.domain.review.domain.service.StoreRatingCalculator;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.UserDetails;
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
    private final StoreRatingCalculator storeRatingCalculator;
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
                .rolecheck(roleCheck)
                .reviewerCheck(reviewerCheck)
                .userDetails(userDetails)
                .build();

        reviewRepository.save(review);

        // 이벤트 발행


        return review.getId().getId();
    }
}
