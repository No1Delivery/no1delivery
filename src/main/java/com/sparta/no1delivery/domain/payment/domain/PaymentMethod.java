package com.sparta.no1delivery.domain.payment.domain;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;

import java.util.Arrays;


public enum PaymentMethod {
    CARD("카드"),
    TRANSFER("계좌이체"),
    VIRTUAL_ACCOUNT("가상계좌"),
    MOBILE_PHONE("휴대폰"),
    CULTURE_GIFT_CERTIFICATE("문화상품권"),
    FOREIGN_EASY_PAY("해외간편결제");

    private String description;

    PaymentMethod(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void validateCardPayment(){
        if(this != CARD){
            throw new CustomException(ErrorCode.INVALID_PAYMENT_METHOD);
        }
    }

    public static PaymentMethod of(String description){
        return Arrays.stream(PaymentMethod.values())
                .filter(p -> p.description.equals(description))
                .findFirst()
                .orElse(null);
    }
}
