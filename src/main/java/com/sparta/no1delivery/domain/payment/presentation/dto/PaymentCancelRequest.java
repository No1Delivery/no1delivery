package com.sparta.no1delivery.domain.payment.presentation.dto;

public record PaymentCancelRequest(
        String orderId,
        String reason
) {
}
