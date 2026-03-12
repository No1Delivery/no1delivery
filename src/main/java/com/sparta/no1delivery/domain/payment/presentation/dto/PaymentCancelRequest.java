package com.sparta.no1delivery.domain.payment.presentation.dto;

import java.util.UUID;

public record PaymentCancelRequest(
        UUID orderId,
        String reason
) {
}
