package com.sparta.no1delivery.domain.payment.infrastructure.dto;

import com.sparta.no1delivery.domain.payment.domain.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TossApproveResponse(
        boolean success,
        String reason,
        String key,
        PaymentStatus status,
        LocalDateTime approvedAt,
        String paymentLog,
        int approvedAmount
) {
}
