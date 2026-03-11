package com.sparta.no1delivery.domain.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewOrderInfo {

    @Column(length = 45, nullable = false)
    private UUID orderId; // 주문 번호

    private UUID storeId; // 상점 ID
    private String storeName; // 상점 이름

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "order_items")
    private List<ReviewOrderItem> items; // 주문상품 목록

    @Builder
    protected ReviewOrderInfo (UUID orderId, UUID storeId, String storeName,
                               List<ReviewOrderItem> items) {
        this.orderId = orderId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.items = items;
    }
}
