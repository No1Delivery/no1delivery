package com.sparta.no1delivery.domain.payment.domain;

public interface PaymentClient {
    PaymentApproveResponse requestApprove(String paymentKey, String orderId, Long amount,String idempotencyKey);
    PaymentApproveResponse requestCancel(String paymentKey, String reason,String idempotencyKey);
}
