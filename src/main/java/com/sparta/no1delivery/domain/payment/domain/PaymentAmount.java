package com.sparta.no1delivery.domain.payment.domain;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor
public class PaymentAmount {

    @Column
    private Long value;


    public PaymentAmount(Long value){
        validate(value);
        this.value = value;
    }

    private void validate(Long value){
        if (value < 0){
            throw new CustomException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }
}
