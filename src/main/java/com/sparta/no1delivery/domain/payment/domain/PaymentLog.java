package com.sparta.no1delivery.domain.payment.domain;

import java.time.LocalDateTime;

public record PaymentLog(
        LocalDateTime datetime,
        String log
) {}
