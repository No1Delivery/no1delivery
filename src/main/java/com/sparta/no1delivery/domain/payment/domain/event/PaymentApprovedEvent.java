package com.sparta.no1delivery.domain.payment.domain.event;

import java.util.UUID;

public record PaymentApprovedEvent(UUID orderId) {

}
