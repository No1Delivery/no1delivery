package com.sparta.no1delivery.domain.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Embeddable
public class PaymentOrderInfo {

    @Column(length = 45, nullable = false, updatable = false)
    private UUID orderId;

    @Column(length = 45, nullable = false, updatable = false)
    private String orderName;

    @Builder
    protected PaymentOrderInfo(UUID orderId, String orderName) {
        this.orderId = orderId;
        this.orderName = orderName;
    }

}
