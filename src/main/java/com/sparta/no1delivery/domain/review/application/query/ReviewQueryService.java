package com.sparta.no1delivery.domain.review.application.query;

import com.sparta.no1delivery.domain.review.application.ReviewServiceDto;
import com.sparta.no1delivery.domain.review.domain.ReviewId;
import com.sparta.no1delivery.domain.review.domain.query.ReviewQueryDto;
import com.sparta.no1delivery.domain.review.domain.query.ReviewQueryRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sparta.no1delivery.domain.review.application.ReviewServiceDto.ReviewDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryService {

    private final ReviewQueryRepository reviewQueryRepository;

    // 리뷰 상세 조회
    public ReviewServiceDto.ReviewDto getReview(UUID reviewId) {
        return reviewQueryRepository.findById(ReviewId.of(reviewId))
                .map(ReviewDto::from)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    // 목록 조회
    public Page<ReviewDto> getReviewsByStore(UUID storeId, ReviewQueryDto.Search search, Pageable pageable) {
        return reviewQueryRepository.findAllByStore(storeId, search, pageable)
                .map(ReviewDto::from);
    }

    public Page<ReviewDto> getReviewsByUser(Long userId, ReviewQueryDto.Search search, Pageable pageable) {
        return reviewQueryRepository.findAllByUser(userId, search, pageable)
                .map(ReviewDto::from);
    }

    public Page<ReviewDto> getAllReviews(ReviewQueryDto.Search search, Pageable pageable) {
        return reviewQueryRepository.findAll(search, pageable)
                .map(ReviewDto::from);
    }

}
