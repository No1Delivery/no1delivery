package com.sparta.no1delivery.domain.user.domain.entity;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    private String detailAddress;

    private Boolean isDefault;

    @Builder
    private UserAddress(BigDecimal latitude,
                        BigDecimal longitude,
                        String address,
                        String detailAddress,
                        Boolean isDefault) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault != null && isDefault;
    }

    public void updateAddress(String address,
                              String detailAddress,
                              BigDecimal latitude,
                              BigDecimal longitude) {

        if (address == null || address.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_INPUT_VALUE);
        }

        this.address = address;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
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
