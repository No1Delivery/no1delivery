package com.sparta.no1delivery.domain.payment.infrastructure.dto;

public record TossApproveResponse(
        String paymentKey,
        String orderId,
        Long totalAmount,
        String approvedAt
) {
}
