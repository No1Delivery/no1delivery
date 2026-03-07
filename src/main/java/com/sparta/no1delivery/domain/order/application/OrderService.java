package com.sparta.no1delivery.domain.order.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.no1delivery.domain.order.application.dto.OrderServiceDto;
import com.sparta.no1delivery.domain.order.domain.*;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    // 주문 생성
    public UUID createOrder(OrderServiceDto.Create dto, Long userId) {

        // 주문 항목 검증
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_ITEM_EMPTY);
        }

        // DTO → OrderItem 변환
        List<OrderItem> items = dto.getItems().stream()
                .map(this::toOrderItem)
                .toList();

        // Store 정보 생성
        StoreInfo storeInfo = new StoreInfo(
                dto.getStoreId(),
                dto.getStoreName()
        );

        // Delivery 정보 생성
        DeliveryInfo deliveryInfo = new DeliveryInfo(
                dto.getDeliveryAddress(),
                dto.getDeliveryAddressDetail(),
                dto.getDeliveryMemo()
        );

        // Order 생성
        Order order = Order.createOrder(
                userId,
                dto.getOrdererName(),
                dto.getPhone(),
                storeInfo,
                deliveryInfo,
                items
        );

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getOrderId();
    }


    // DTO Item → OrderItem 변환
    private OrderItem toOrderItem(OrderServiceDto.Item item) {

        // 클라이언트에서 전달한 메뉴 가격 검증
        if (item.getMenuPrice() <= 0) {
            throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
        }

        // 옵션 리스트 null 방지
        List<OrderServiceDto.Option> options =
                item.getOptions() == null ? Collections.emptyList() : item.getOptions();

        // 옵션 JSON 스냅샷 생성
        String optionJson = convertOptionToJson(options);

        return new OrderItem(
                item.getMenuId(),
                item.getMenuName(),
                optionJson,
                item.getQuantity(),
                item.getMenuPrice(),
                options
        );
    }


    // 옵션 객체 → JSON 변환
    private String convertOptionToJson(List<OrderServiceDto.Option> options) {

        if (options == null || options.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    // 주문 취소
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.cancel();
    }


    // 주문 상태 변경 (분기 처리)
    public void changeOrderStatus(UUID orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        switch (status) {

            case ORDER_ACCEPT -> order.orderAccept();

            case PREPARING -> order.startPreparing();

            case READY -> order.ready();

            case DELIVERY -> order.startDelivery();

            case DELIVERY_DONE -> order.deliveryDone();

            case ORDER_DONE -> order.complete();

            default -> throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }
}