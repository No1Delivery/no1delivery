package com.sparta.no1delivery.domain.user.domain.entity;

import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_user_address")
public class UserAddress {
    @Id
    @GeneratedValue
    @Column(name = "address_idx", nullable = false, updatable = false)
    private UUID addressIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double latitude;

    private Double longitude;

    private String address;

    private String detailAddress;

    private Boolean isDefault;

    @Builder
    public UserAddress(String address,
                        String detailAddress,
                        AddressToCoords addressToCoords,
                        Boolean isDefault) {
        this.address = address;
        this.detailAddress = detailAddress;

        if (!StringUtils.hasText(address) || !StringUtils.hasText(detailAddress)) return;
        double[] coords = addressToCoords.convert(address);
        latitude = coords[0];
        longitude = coords[1];

        this.isDefault = isDefault != null && isDefault;
    }

    public void updateAddress(String address,
                              String detailAddress,
                              AddressToCoords addressToCoords) {

        if (address == null || address.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_INPUT_VALUE);
        }
        this.address = address;
        this.detailAddress = detailAddress;
        if (!StringUtils.hasText(address) || !StringUtils.hasText(detailAddress)) return;
        double[] coords = addressToCoords.convert(address);
        latitude = coords[0];
        longitude = coords[1];
    }

    public void updateDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDefault() {
        return this.isDefault;
    }
}
