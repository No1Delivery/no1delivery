package com.sparta.no1delivery.domain.order.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class OrderServiceDto {

    //주문 생성 Service DTO
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

    //주문 상세 DTO
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

    // 옵션 그룹 (예: 맵기, 사이즈)
    @Getter
    @Builder
    public static class Option {
        private String name;
        private int price;
        private List<SubOption> subOptions;
    }

    // 옵션 상세 (예: 매운맛, 보통맛)
    @Getter
    @Builder
    public static class SubOption {
        private String name;
        private int price;
    }
}