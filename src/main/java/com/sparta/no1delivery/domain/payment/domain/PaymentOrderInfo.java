package com.sparta.no1delivery.domain.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentOrderInfo {

    @Column
    private UUID orderId;

    @Column
    private String orderName;



}
