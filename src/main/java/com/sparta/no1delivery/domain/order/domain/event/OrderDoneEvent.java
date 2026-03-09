package com.sparta.no1delivery.domain.order.domain.event;

import java.util.UUID;

// 주문 완료 이벤트
public record OrderDoneEvent(UUID orderId) {}