package com.sparta.no1delivery.domain.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryInfo {

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "request_memo")
    private String requestMessage;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    public DeliveryInfo(String address,
                        String detailAddress,
                        String requestMessage,
                        String phone) {

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("주소는 필수입니다.");
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("연락처는 필수입니다.");
        }

        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
        this.phone = phone;
    }
}