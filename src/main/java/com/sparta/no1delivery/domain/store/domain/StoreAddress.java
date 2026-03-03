package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreAddress {

    @Column(name = "address")
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "latitude")
    private double latitude; // 위도

    @Column(name = "longitude")
    private double longitude; // 경도

    protected StoreAddress(String address, String detailAddress, AddressToCoords addressToCoords) {
        this.address = address;
        this.detailAddress = detailAddress;

        if (!StringUtils.hasText(address) || !StringUtils.hasText(detailAddress)) return;

        double[] coords = addressToCoords.convert(address);
        latitude = coords[0];
        longitude = coords[1];

    }
}
