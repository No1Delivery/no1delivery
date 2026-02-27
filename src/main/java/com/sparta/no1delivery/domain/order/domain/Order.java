package com.sparta.no1delivery.domain.order.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "P_Order")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "orderer_id")
    private Long ordererId;

    @Column(name = "orderer_name")
    private String ordererName;

    @Column(name = "address")
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "store_id")
    private UUID storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "requestMemo")
    private String requestMemo;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> orderItems;

    // 주문 생성 시 필수 로직
    public static Order createOrder(Long ordererId,
                                    String ordererName,
                                    String address,
                                    String detailAddress,
                                    UUID storeId,
                                    String storeName,
                                    String requestMemo,
                                    List<OrderItem> orderItems) {

        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 최소 1개 이상이어야 합니다.");
        }

        Order order = Order.builder()
                .status(OrderStatus.ORDERED)
                .ordererId(ordererId)
                .ordererName(ordererName)
                .address(address)
                .detailAddress(detailAddress)
                .storeId(storeId)
                .storeName(storeName)
                .requestMemo(requestMemo)
                .orderItems(orderItems)
                .build();

        // 연관관계 편의 메서드
        orderItems.forEach(item -> item.setOrder(order));

        // 총 금액 계산
        order.totalPrice = order.calculateTotalPrice();

        return order;
    }

    // 주문 총 금액 계산
    private int calculateTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getSubtotalPrice)
                .sum();
    }

    // 주문 취소
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
        this.canceledAt = LocalDateTime.now();
    }
}