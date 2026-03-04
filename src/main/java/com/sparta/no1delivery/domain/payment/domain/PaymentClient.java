package com.sparta.no1delivery.domain.payment.domain;

public interface PaymentClient{
    PaymentApproveResponse requestApprove(String paymentKey, String orderId, Long amount);
    // payment키와 orderId, amount값을 함께 담아서 토스API에 전달하겠다는 규칙
}