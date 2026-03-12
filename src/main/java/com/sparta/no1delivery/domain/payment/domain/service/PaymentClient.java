package com.sparta.no1delivery.domain.payment.domain.service;

import com.sparta.no1delivery.domain.payment.domain.PaymentId;

import java.util.UUID;

public interface PaymentClient {
    PaymentClientDto requestApprove(String paymentKey, UUID orderId, Long amount);
    PaymentClientDto requestCancel(PaymentId paymentId, String paymentKey, String reason);
}
