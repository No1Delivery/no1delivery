package com.sparta.no1delivery.domain.order.domain.exception;

// 주문 자체가 존재하지 않을 때
// TODO: 클라이언트 구현 완료 후 HttpStatusCodeException 상속으로 변경 해야함
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }
}