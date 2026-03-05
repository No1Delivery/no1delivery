package com.sparta.no1delivery.domain.order.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class OrderServiceDto {

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

    @Getter
    @Builder
    public static class Item {
        private UUID menuId;
        private String menuName;
        private String menuOption;
        private int quantity;
        private int menuPrice;
    }
}