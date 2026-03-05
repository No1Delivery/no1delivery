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

    // 주문 ID (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 45)
    private OrderStatus status;

    // 주문 취소 시간
    private LocalDateTime canceledAt;

    // 총 주문 금액
    private int totalPrice;

    // 주문자 정보
    @Embedded
    private Orderer orderer;

    // 가게 정보
    @Embedded
    private StoreInfo storeInfo;

    // 배송 정보
    @Embedded
    private DeliveryInfo deliveryInfo;

    // 주문 상품 목록
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();


    // 주문 생성
    public static Order createOrder(Long ordererId,
                                    String ordererName,
                                    String phone,
                                    StoreInfo storeInfo,
                                    DeliveryInfo deliveryInfo,
                                    List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_ITEM_EMPTY);
        }

        Order order = new Order();
        order.status = OrderStatus.ORDER_CREATING;

        // 주문자 정보 생성
        order.orderer = new Orderer(ordererId, ordererName, phone);

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


    // 주문 접수
    public void orderAccept() {

        if (this.status != OrderStatus.ORDER_CREATING) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.ORDER_ACCEPT;
    }


    // 결제 확인
    public void paymentConfirm() {

        if (this.status != OrderStatus.ORDER_ACCEPT) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.PAYMENT_CONFIRM;
    }


    // 배달 시작
    public void startDelivery() {

        if (this.status != OrderStatus.PAYMENT_CONFIRM) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.DELIVERY;
    }


    // 배달 완료
    public void deliveryDone() {

        if (this.status != OrderStatus.DELIVERY) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.DELIVERY_DONE;
    }


    // 주문 완료
    public void complete() {

        if (this.status != OrderStatus.DELIVERY_DONE) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.ORDER_DONE;
    }


    // 주문 취소 (주문 접수 후 5분 이내 취소 가능)
    public void cancel() {

        if (this.status == OrderStatus.ORDER_CANCEL) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        if (this.status == OrderStatus.ORDER_ACCEPT) {

            if (createdAt == null || LocalDateTime.now().isBefore(createdAt.plusMinutes(5))) {
                this.status = OrderStatus.ORDER_CANCEL;
                this.canceledAt = LocalDateTime.now();
                return;
            }

            throw new CustomException(ErrorCode.ORDER_CANCEL_TIME_EXPIRED);
        }

        if (this.status == OrderStatus.PAYMENT_CONFIRM) {
            this.status = OrderStatus.ORDER_REFUND;
            return;
        }

        throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }


    // Soft Delete
    public void markDeleted(Long userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

}