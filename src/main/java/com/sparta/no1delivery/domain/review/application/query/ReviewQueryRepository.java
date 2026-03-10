package com.sparta.no1delivery.domain.review.application.query;

import com.sparta.no1delivery.domain.review.domain.ReviewId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryRepository {

    private final ReviewQueryRepository reviewQueryRepository;

    // 리뷰 상세 조회

}
