package com.sparta.no1delivery.domain.payment.domain;

import com.sparta.no1delivery.domain.payment.infrastructure.dto.TossApproveResponse;

public interface PaymentClient {
    TossApproveResponse requestApprove(String paymentKey, String orderId, Long amount, String idempotencyKey);
    TossApproveResponse requestCancel(String paymentKey, String reason, String idempotencyKey);
}
