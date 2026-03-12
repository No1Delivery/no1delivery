package com.sparta.no1delivery.domain.payment.domain.service;

import com.sparta.no1delivery.domain.payment.domain.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentClientDto(
        boolean success,
        String reason,
        String key,
        PaymentStatus status,
        LocalDateTime approvedAt,
        String paymentLog,
        int approvedAmount
) {
}
