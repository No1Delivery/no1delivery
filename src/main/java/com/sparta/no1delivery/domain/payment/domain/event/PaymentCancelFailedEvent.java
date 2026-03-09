package com.sparta.no1delivery.domain.payment.domain.event;

import java.util.UUID;

public record PaymentCancelFailedEvent(
        UUID orderId,
        String errorCode,
        String errorMessage
) {
}
