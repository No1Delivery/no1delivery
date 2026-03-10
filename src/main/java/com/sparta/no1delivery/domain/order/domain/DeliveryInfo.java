package com.sparta.no1delivery.domain.order.domain;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryInfo {

    @Column( nullable = false)
    private String address;

    private String detailAddress;

    @Column(name = "request_memo")
    private String requestMessage;

    public DeliveryInfo(String address,
                        String detailAddress,
                        String requestMessage) {

        if (address == null || address.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_INPUT_VALUE);
        }

        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }
}