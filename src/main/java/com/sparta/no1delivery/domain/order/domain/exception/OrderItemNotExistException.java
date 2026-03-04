package com.sparta.no1delivery.domain.order.domain.exception;

//주문 항목이 존재하지 않을 때
// TODO: 클라이언트 구현 완료 후 HttpStatusCodeException 상속으로 변경 헤야함
public class OrderItemNotExistException extends RuntimeException {
    public OrderItemNotExistException(String message) {
        super(message);
    }

    public OrderItemNotExistException() {
        super("주문상품이 누락되었습니다.");
    }
}