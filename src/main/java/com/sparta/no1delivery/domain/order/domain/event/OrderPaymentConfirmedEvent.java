package com.sparta.no1delivery.domain.order.domain.event;

import java.util.UUID;

public record OrderPaymentConfirmedEvent(
        UUID orderId
) {}