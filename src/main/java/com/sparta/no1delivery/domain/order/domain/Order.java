package com.sparta.no1delivery.domain.order.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "P_Order")
@SQLRestriction("deleted_at IS NULL")
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

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();


    // 주문 생성
    public static Order createOrder(Long ordererId,
                                    String ordererName,
                                    StoreInfo storeInfo,
                                    DeliveryInfo deliveryInfo,
                                    List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_ITEM_EMPTY);
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

    // 연관관계 편의 메서드
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    // 총 주문 금액 계산
    private void calculateAndSetTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getSubtotalPrice)
                .sum();
    }

    // 주문 취소
    public void cancel() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        this.status = OrderStatus.CANCELLED;
        this.canceledAt = LocalDateTime.now();
    }

    // Soft Delete
    public void markDeleted(Long userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }
}