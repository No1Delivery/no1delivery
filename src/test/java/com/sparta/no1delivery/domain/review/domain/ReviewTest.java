package com.sparta.no1delivery.domain.review.domain;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.OrderStatus;
import com.sparta.no1delivery.domain.review.domain.exception.ReviewException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReviewTest {

    @Test
    void 배송완료가_아니면_리뷰작성_불가() {

        // given
        Order order = createOrder(1L, OrderStatus.ORDERED);

        // when & then
        assertThrows(ReviewException.class, () ->
                Review.create(
                        1L,
                        "홍길동",
                        UUID.randomUUID(),
                        "치킨집",
                        5,
                        "맛있어요",
                        order
                )
        );
    }

    @Test
    void 주문자가_아니면_리뷰_작성_불가() {

        // given
        Order order = createOrder(2L, OrderStatus.DELIVERED);

        // when & then
        assertThrows(ReviewException.class, () ->
                Review.create(
                        1L,
                        "홍길동",
                        UUID.randomUUID(),
                        "치킨집",
                        5,
                        "맛있어요",
                        order
                )
        );
    }
    @Test
    void 평점이_범위를_벗어나면_예외() {

        Order order = createOrder(1L, OrderStatus.DELIVERED);

        assertThrows(ReviewException.class, () ->
                Review.create(
                        1L,
                        "홍길동",
                        UUID.randomUUID(),
                        "치킨집",
                        10,
                        "맛있어요",
                        order
                )
        );
    }

    private Order createOrder(Long ordererId, OrderStatus status) {
        Order order = mock(Order.class);

        when(order.getOrdererId()).thenReturn(ordererId);
        when(order.getStatus()).thenReturn(status);

        return order;
    }
}