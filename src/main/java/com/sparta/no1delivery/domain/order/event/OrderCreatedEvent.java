package com.sparta.no1delivery.domain.order.event;

import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        String orderName,
        Long amount
) {
}
