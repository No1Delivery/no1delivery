package com.sparta.no1delivery.domain.payment.presentation.dto;

import java.util.UUID;

public record PaymentConfirmRequest(
        String paymentKey,
        UUID orderId,
        Long amount
) {

}