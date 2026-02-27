package com.sparta.no1delivery.domain.order.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
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
public class Order extends BaseUserEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;  //주문 상태

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; //주문 취소 시간

    @Column(name = "total_price")
    private int totalPrice; //주문 총 금액

    @Column(name = "orderer_id")
    private Long ordererId; //주문자 Id

    @Column(name = "orderer_name")
    private String ordererName; //주문자 이름

    @Column(name = "address")
    private String address;  // 배송 주소

    @Column(name = "detail_address")
    private String detailAddress; // 배송 상세주소

    @Column(name = "store_id")
    private UUID storeId;  //가게 Id

    @Column(name = "store_name")
    private String storeName;  //가게 이름

    @Column(name = "requestMemo")
    private String requestMemo; // 고객 요청 사항

    @Column(length = 45)
    private String deletedBy;   // soft delete 기록: 누가 삭제했는지

    @Column
    private LocalDateTime deletedAt; // soft delete 기록: 언제 삭제했는지

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> orderItems; // 주문 항목 리스트


    // soft delete 메서드
    public void markDeleted(String username) {
        this.deletedBy = username;
        this.deletedAt = LocalDateTime.now();
    }


    // 총합 계산 setter 추가
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

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
        order.setTotalPrice(order.calculateTotalPrice());

        return order;
    }

    //주문 총 금액 계산
    private int calculateTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getSubtotalPrice)
                .sum();
    }

    // 주문 취소 ->  주문 상태 변경 + 취소 시간 기록
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
        this.canceledAt = LocalDateTime.now();
    }
}