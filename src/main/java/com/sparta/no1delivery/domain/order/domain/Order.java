package com.sparta.no1delivery.domain.order.domain;

import com.sparta.no1delivery.domain.order.domain.event.OrderAcceptedEvent;
import com.sparta.no1delivery.domain.order.domain.event.OrderDoneEvent;
import com.sparta.no1delivery.domain.order.domain.event.OrderPaymentConfirmedEvent;
import com.sparta.no1delivery.domain.order.domain.event.OrderRefundedEvent;
import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

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
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private OrderStatus status;

    private LocalDateTime canceledAt;

    private int totalPrice;

    @Embedded
    private Orderer orderer;

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

    // ===== 도메인 이벤트 =====
    @Transient
    private final List<Object> domainEvents = new ArrayList<>();


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

        order.orderer = new Orderer(ordererId, ordererName, phone);
        order.storeInfo = storeInfo;
        order.deliveryInfo = deliveryInfo;

        items.forEach(order::addOrderItem);
        order.calculateAndSetTotalPrice();

        return order;
    }


    // 연관 관계 편의 메서드
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

    // 배송지 변경
    public void changeDeliveryInfo(String address, String detailAddress, String memo) {

        // 주문 접수 전 까지만 변경 가능
        if (this.status != OrderStatus.ORDER_CREATING) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.deliveryInfo = new DeliveryInfo(
                address,
                detailAddress,
                memo
        );
    }


    // 주문 접수
    public void orderAccept() {

        if (this.status != OrderStatus.ORDER_CREATING) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.ORDER_ACCEPT;

        registerEvent(
                new OrderAcceptedEvent(
                        this.orderId,
                        this.orderer.getName(),
                        (long) this.totalPrice,
                        System.currentTimeMillis()
                )
        );
    }


    // 결제 확인
    public void paymentConfirm() {

        if (this.status != OrderStatus.ORDER_ACCEPT) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.PAYMENT_CONFIRM;

        registerEvent(new OrderPaymentConfirmedEvent(this.orderId));
    }


    // 조리 시작
    public void startPreparing() {

        if (this.status != OrderStatus.PAYMENT_CONFIRM) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.PREPARING;
    }


    // 조리 완료
    public void ready() {

        if (this.status != OrderStatus.PREPARING) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.READY;
    }


    // 배송 시작
    public void startDelivery() {

        if (this.status != OrderStatus.READY) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.DELIVERY;
    }


    // 배송 완료
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

        registerEvent(new OrderDoneEvent(this.orderId));
    }


    // 주문 취소
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

            registerEvent(new OrderRefundedEvent(this.orderId));
            return;
        }

        throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
    }


    // 도메인 이벤트

    private void registerEvent(Object event) {
        domainEvents.add(event);
    }

    @DomainEvents
    public List<Object> domainEvents() {
        return domainEvents;
    }

    @AfterDomainEventPublication
    public void clearEvents() {
        domainEvents.clear();
    }


    //S o f t Delete
    public void remove(UserDetails userDetails) {
        delete(userDetails);
    }

}