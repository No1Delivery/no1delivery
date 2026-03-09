package com.sparta.no1delivery.domain.payment.domain;

import java.time.LocalDateTime;

public record PaymentAmountDto(
        String paymentKey,
        Long approvedAmount,
        LocalDateTime approvedAt,
        String paymentLog
) {}

