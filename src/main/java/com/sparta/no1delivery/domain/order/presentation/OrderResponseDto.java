package com.sparta.no1delivery.domain.order.presentation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderResponseDto {

    //주문 목록 조회용
    @Getter
    @Builder
    public static class Order {
        private UUID orderId;
        private String storeName;
        private int totalOrderPrice;
        private String status;
        private LocalDateTime createdAt;
    }

    // 주문 생성 응답용
    @Getter
    @Builder
    public static class Create {
        private UUID orderId;
        private String status;
    }

    //주문 상세 조회용
    @Getter
    @Builder
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

    // 상세 안에 들어가는 주문 아이템
    @Getter
    @Builder
    public static class OrderItem {
        private String itemName;
        private int quantity;
        private int price;
        private int totalPrice;
    }
}