package com.sparta.no1delivery.domain.payment.presentation.dto;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        Long amount
) {

}