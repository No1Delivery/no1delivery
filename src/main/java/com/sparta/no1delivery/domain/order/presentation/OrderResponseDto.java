package com.sparta.no1delivery.domain.order.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderResponseDto {

    // 주문 생성 응답
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        private UUID orderId;
    }

    // 주문 목록 조회용
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Order {
        private UUID orderId;
        private String storeName;
        private int totalOrderPrice;
        private String status;
        private LocalDateTime createdAt;
    }

    // 주문 상세 조회용
    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderDetail {
        private UUID orderId;
        private String storeName;
        private String ordererName;
        private String deliveryAddress;
        private String deliveryMemo;
        private int totalOrderPrice;
        private String status;
        private List<OrderItem> items;
        private LocalDateTime createdAt;
    }

    // 주문 상세 안의 아이템
    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderItem {
        private String itemName;
        private int quantity;
        private int price;
        private int totalPrice;
    }
}