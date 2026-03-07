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
    private Long value; // 가격을 저장하기 위한 필드


    public PaymentAmount(Long value){ //들어온 값이 유효하다면 값으로 받아서 상자를 조립하는 생성자
        validate(value);
        this.value = value;
    }

    private void validate(Long value){ //들어온 값이 0보다 큰지 확인을 위해(유효성 체크) 메서드
        if (value < 0){
            throw new CustomException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }

    // 위변조 도메인 로직
    public void verifyAmount(Long requestamount){
        if(this.value != requestamount ){
            throw new CustomException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }
    }
}
