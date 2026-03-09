package com.sparta.no1delivery.domain.payment.domain.event;

import java.util.UUID;

public record PaymentFailedEvent(
        UUID orderId,
        String errorMessage
) {
}
