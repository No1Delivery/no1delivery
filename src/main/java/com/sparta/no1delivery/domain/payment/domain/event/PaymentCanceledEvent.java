package com.sparta.no1delivery.domain.payment.domain.event;

import java.util.UUID;

public record PaymentCanceledEvent(
        UUID orderId,
        Long canceledAmount
) {
}
