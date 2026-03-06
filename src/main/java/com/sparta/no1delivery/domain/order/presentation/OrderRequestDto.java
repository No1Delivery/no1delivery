package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.dto.OrderServiceDto;
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

    public OrderServiceDto.Create toServiceDto() {

        return OrderServiceDto.Create.builder()
                .ordererName(ordererName)
                .storeId(storeId)
                .storeName(storeName)
                .deliveryAddress(deliveryAddress)
                .deliveryAddressDetail(deliveryAddressDetail)
                .deliveryMemo(deliveryMemo)
                .phone(phone)
                .items(
                        items.stream()
                                .map(item -> OrderServiceDto.Item.builder()
                                        .menuId(item.getMenuId())
                                        .menuName(item.getMenuName())
                                        .menuOption(item.getMenuOption())
                                        .quantity(item.getQuantity())
                                        .menuPrice(item.getMenuPrice())
                                        .build())
                                .toList()
                )
                .build();
    }

    @Getter
    public static class OrderItemRequest {
        private UUID menuId;
        private String menuName;
        private String menuOption;
        private int quantity;
        private int menuPrice;
    }
}