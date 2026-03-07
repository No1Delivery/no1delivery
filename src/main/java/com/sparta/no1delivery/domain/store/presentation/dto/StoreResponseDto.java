package com.sparta.no1delivery.domain.store.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreStatus;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StoreResponseDto(
        UUID id,
        String name,
        String description,
        String phone,
        String address,
        String detailAddress,
        StoreStatus status,
        Double rating,
        Integer reviewCount,
        String ownerName
) {
    public static StoreResponseDto fromList(Store store) {
        return new StoreResponseDto(
                store.getId().getId(),
                store.getName(),
                store.getDescription(),
                null,
                null,
                null,
                store.getStatus(),
                store.getRating().getAverage(),
                store.getRating().getCount(),
                null
        );
    }

    public static StoreResponseDto fromDetail(Store store) {
        return new StoreResponseDto(
                store.getId().getId(),
                store.getName(),
                store.getDescription(),
                store.getPhone(),
                store.getAddress().getAddress(),
                store.getAddress().getDetailAddress(),
                store.getStatus(),
                store.getRating().getAverage(),
                store.getRating().getCount(),
                store.getOwner().getName()
        );
    }
}