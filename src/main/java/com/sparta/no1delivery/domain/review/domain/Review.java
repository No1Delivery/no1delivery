package com.sparta.no1delivery.domain.review.domain;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.OrderStatus;
import com.sparta.no1delivery.domain.review.domain.exception.ReviewException;
import com.sparta.no1delivery.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name="P_Review")
@Getter
public class Review extends BaseEntity {

    @EmbeddedId
    private ReviewId reviewId;

    private Long reviewerId;     // 작성자 ID (JWT에서 전달)
    private String reviewerName; // 작성 당시 이름
    private UUID storeId;        // 가게 ID
    private String storeName;    // 가게 이름
    private Integer rating;
    private String comment;

    @OneToOne
    @JoinColumn(name="order_id")
    private Order order;

    protected Review() {}

    private Review(Long reviewerId, String reviewerName, UUID storeId, String storeName,
                   Integer rating, String comment, Order order) {
        this.reviewId = ReviewId.of();
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.storeId = storeId;
        this.storeName = storeName;
        this.rating = rating;
        this.comment = comment;
        this.order = order;
        validate(order);
    }

    public static Review create(Long reviewerId, String reviewerName, UUID storeId, String storeName,
                                Integer rating, String comment, Order order) {
        return new Review(reviewerId, reviewerName, storeId, storeName, rating, comment, order);
    }

    private void validate(Order order) {
        if (!reviewerId.equals(order.getOrdererId())) {
            throw new ReviewException("주문자만 작성 가능");
        }
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ReviewException("주문 완료 후 작성 가능");
        }
    }
}