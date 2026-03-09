package com.sparta.no1delivery.domain.order.domain;

public enum OrderStatus {

    ORDER_CREATING,      // 주문 생성 중
    ORDER_ACCEPT,        // 주문 접수
    PAYMENT_CONFIRM,     // 결제 완료

    PREPARING,           // 조리중
    READY,               // 준비 완료

    DELIVERY,            // 배송중
    DELIVERY_DONE,       // 배송 완료
    ORDER_DONE,          // 주문 최종 완료

    ORDER_CANCEL,        // 주문 취소
    ORDER_REFUND         // 환불
}