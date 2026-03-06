package com.sparta.no1delivery.domain.order.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.no1delivery.domain.order.application.dto.OrderServiceDto;
import com.sparta.no1delivery.domain.order.domain.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    // 옵션 객체 → JSON 변환을 위한 ObjectMapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 주문 생성
    public UUID createOrder(OrderServiceDto.Create dto, Long userId) {

        // OrderItem 변환
        List<OrderItem> items = dto.getItems() == null ? List.of() :
                dto.getItems().stream()
                        .map(item -> {

                            // 옵션을 JSON 문자열로 변환
                            String optionJson = convertOptionToJson(item.getOptions());

                            return new OrderItem(
                                    item.getMenuId(),
                                    item.getMenuName(),
                                    optionJson,
                                    item.getQuantity(),
                                    item.getMenuPrice()
                            );
                        })
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

    // 옵션 객체 → JSON 변환
    private String convertOptionToJson(List<OrderServiceDto.Option> options) {
        if (options == null || options.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("옵션 JSON 변환 실패", e);
        }
    }

    // 주문 취소
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        order.cancel();
    }
}