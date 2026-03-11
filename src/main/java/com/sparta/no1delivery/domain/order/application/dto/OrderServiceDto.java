package com.sparta.no1delivery.domain.order.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class OrderServiceDto {

    // 주문 생성 DTO
    @Getter
    @Builder
    public static class Create {
        private String ordererName;
        private UUID storeId;
        private String storeName;
        private String deliveryAddress;
        private String deliveryAddressDetail;
        private String deliveryMemo;
        private String phone;
        private List<Item> items;
    }

    // 주문 상품
    @Getter
    @Builder
    public static class Item {
        private UUID menuId;
        private String menuName;
        private int quantity;
        private int menuPrice;

        // 선택된 옵션 목록
        private List<Option> options;
    }

    // 옵션 그룹 (예: 사이즈, 토핑)
    @Getter
    @Builder
    public static class Option {
        private String name;
        private List<SubOption> subOptions;
    }

    // 실제 선택 옵션 (가격 포함)
    @Getter
    @Builder
    public static class SubOption {
        private String name;
        private int price;
    }
}