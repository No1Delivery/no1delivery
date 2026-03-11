package com.sparta.no1delivery.domain.payment.presentation.dto;

public record PaymentFailRequest(
        String code,
        String message
) {
}
