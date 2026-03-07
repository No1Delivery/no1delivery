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

        // OrderItem 변환 (가독성을 위해 메서드 분리)
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
        // 가격이 0 이하인 경우 잘못된 요청으로 판단 (가격 조작 방지)
        if (item.getMenuPrice() <= 0) {
            throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
        }

        // 옵션 리스트 null 방지
        List<OrderServiceDto.Option> options =
                item.getOptions() == null ? Collections.emptyList() : item.getOptions();

        // 옵션을 JSON 문자열로 변환 (주문 시점 옵션 스냅샷 저장)
        String optionJson = convertOptionToJson(options);

        // 옵션 가격 계산
        int optionPrice = calculateOptionPrice(options);

        // OrderItem 생성
        return new OrderItem(
                item.getMenuId(),
                item.getMenuName(),
                optionJson,
                item.getQuantity(),
                item.getMenuPrice(),
                optionPrice
        );
    }


    // 옵션 객체 → JSON 변환
    private String convertOptionToJson(List<OrderServiceDto.Option> options) {

        // 옵션이 없으면 null 저장
        if (options == null || options.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    // 선택된 옵션들의 총 가격 계산
    // (옵션 그룹 가격 + 하위 옵션 가격)
    private int calculateOptionPrice(List<OrderServiceDto.Option> options) {

        if (options == null || options.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (OrderServiceDto.Option option : options) {

            // 옵션 그룹 가격
            total += option.getPrice();

            // 하위 옵션 null 방지
            if (option.getSubOptions() != null) {

                for (OrderServiceDto.SubOption sub : option.getSubOptions()) {

                    // 하위 옵션 가격 추가
                    total += sub.getPrice();
                }
            }
        }

        return total;
    }


    // 주문 취소
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.cancel();
    }
}