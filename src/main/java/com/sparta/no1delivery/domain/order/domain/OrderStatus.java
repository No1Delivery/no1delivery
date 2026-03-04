package com.sparta.no1delivery.domain.order.domain;

public enum OrderStatus {
    ORDERED, //주문 요청
    PREPARING, //조리중
    READY, //준비 완료
    DELIVERING,  //배달중
    DELIVERED, //배달 완료
    CANCELLED  //주믄 취소
}