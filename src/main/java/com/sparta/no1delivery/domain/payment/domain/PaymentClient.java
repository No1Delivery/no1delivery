package com.sparta.no1delivery.domain.payment.domain;

public interface PaymentClient {
    PaymentAmountDto requestApprove(String paymentKey, String orderId, Long amount, String idempotencyKey);
    PaymentAmountDto requestCancel(String paymentKey, String reason, String idempotencyKey);
}
