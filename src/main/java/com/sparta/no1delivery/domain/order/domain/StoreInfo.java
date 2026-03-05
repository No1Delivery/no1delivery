package com.sparta.no1delivery.domain.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreInfo {

    @Column( nullable = false)
    private UUID storeId;

    @Column( nullable = false)
    private String storeName;

    public StoreInfo(UUID storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }
}