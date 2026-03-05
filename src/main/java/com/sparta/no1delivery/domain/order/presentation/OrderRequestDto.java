package com.sparta.no1delivery.domain.order.presentation;

import lombok.Getter;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderRequestDto {

    private String ordererName;

    private UUID storeId;
    private String storeName;
    private String storeAddress;
    private String storeTel;

    private String deliveryAddress;
    private String deliveryAddressDetail;
    private String deliveryMemo;
    private String phone;

    private List<OrderItemRequest> items;

    @Getter
    public static class OrderItemRequest {
        private UUID menuId;
        private String menuName;
        private String menuOption;
        private int quantity;
        private int menuPrice;
    }
}