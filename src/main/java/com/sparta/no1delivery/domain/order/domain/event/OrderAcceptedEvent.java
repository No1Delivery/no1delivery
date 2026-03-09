package com.sparta.no1delivery.domain.order.domain.event;

import java.util.UUID;

// 주문 접수 이벤트
public record OrderAcceptedEvent(
        UUID orderId,
        long occurredAt
) {}