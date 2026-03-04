package com.sparta.no1delivery.domain.order.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "P_Order")
public class Order extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "orderer_id", nullable = false)
    private Long ordererId;

    @Column(name = "orderer_name", nullable = false)
    private String ordererName;

    @Embedded
    private StoreInfo storeInfo;

    @Embedded
    private DeliveryInfo deliveryInfo;

    @Column(length = 45)
    private String deletedByName;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 생성 로직
    public static Order createOrder(Long ordererId,
                                    String ordererName,
                                    StoreInfo storeInfo,
                                    DeliveryInfo deliveryInfo,
                                    List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 최소 1개 이상이어야 합니다.");
        }

        Order order = new Order();
        order.status = OrderStatus.ORDERED;
        order.ordererId = ordererId;
        order.ordererName = ordererName;
        order.storeInfo = storeInfo;
        order.deliveryInfo = deliveryInfo;

        items.forEach(order::addOrderItem);
        order.calculateAndSetTotalPrice();

        return order;
    }

    // 연관관계 메서드
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    // 비즈니스 로직
    private void calculateAndSetTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getSubtotalPrice)
                .sum();
    }

    public void cancel() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        this.status = OrderStatus.CANCELLED;
        this.canceledAt = LocalDateTime.now();
    }

    public void markDeleted(String username) {
        this.deletedByName = username;
        this.deletedAt = LocalDateTime.now();
    }
}