package com.sparta.no1delivery.review.domain;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.OrderStatus;
import com.sparta.no1delivery.domain.review.domain.Review;
import com.sparta.no1delivery.domain.review.domain.exception.ReviewException;
import org.junit.jupiter.api.Test;

public class ReviewTest {
    @Test
    void 배송완료가_아니면_리뷰작성_불가() {
        // given
        Order order = new Order(OrderStatus.ORDERED); // 배송완료 아님

        // when & then
        assertThrows(ReviewException.class, () -> {
            Review.create(order);
        });
    }
}
