package com.sparta.no1delivery.domain.review.infrastructure.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.domain.review.domain.Review;
import com.sparta.no1delivery.domain.review.domain.ReviewId;
import com.sparta.no1delivery.domain.review.domain.query.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Review> findById(ReviewId reviewId) {

    }
}
