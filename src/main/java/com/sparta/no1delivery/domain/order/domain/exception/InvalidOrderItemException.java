package com.sparta.no1delivery.domain.order.domain.exception;

//주문 항목이 유효하지 않을 때
// TODO: 나중에 글로벌 BadRequestException 상속으로 리팩터링해야함
public class InvalidOrderItemException extends RuntimeException {
    public InvalidOrderItemException(String message) {
        super(message);
    }

    public InvalidOrderItemException() {
        super("주문이 불가한 메뉴가 포함되어 있습니다.");
    }
}