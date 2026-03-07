package com.sparta.no1delivery.domain.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentId {

    @Column(length = 45, name = "payment_id") //UUID로 id 값을 만들기
    private UUID id;


    public static PaymentId of(){ // 기본적으로 자동 랜덤 생성
        return PaymentId.of(UUID.randomUUID());
    }

    public static PaymentId of(UUID id){ // 만일 받아온 UUID가 있다면 그 id를 id로 사용하겠다?
        return new PaymentId(id);
    }
}
