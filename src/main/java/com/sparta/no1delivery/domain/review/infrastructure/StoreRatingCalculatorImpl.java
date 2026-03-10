package com.sparta.no1delivery.domain.review.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.domain.review.domain.QReview;
import com.sparta.no1delivery.domain.review.domain.service.StoreRatingCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StoreRatingCalculatorImpl implements StoreRatingCalculator {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public double getAverageRating(UUID storeId) {
        QReview review = QReview.review;

        Double avg = jpaQueryFactory.select(review.content.score.avg())
                .from(review)
                .where(review.info.storeId.eq(storeId)
                        .and(review.deletedAt.isNull()))
                .fetchOne();

        return avg == null ? 0.0 : avg;
    }
}
