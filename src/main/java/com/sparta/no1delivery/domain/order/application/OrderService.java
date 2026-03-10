package com.sparta.no1delivery.domain.order.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.no1delivery.domain.order.application.dto.OrderServiceDto;
import com.sparta.no1delivery.domain.order.domain.*;
import com.sparta.no1delivery.domain.order.domain.service.OptionCheck;
import com.sparta.no1delivery.domain.order.domain.service.OrderCheck;
import com.sparta.no1delivery.domain.order.domain.service.ProductProvider;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.OwnerCheck;
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

    // 상품 조회 서비스
    private final ProductProvider productProvider;

    // 메뉴 검증 서비스
    private final OrderCheck orderCheck;

    // 옵션 검증 서비스
    private final OptionCheck optionCheck;

    // role 권한 체크
    private final RoleCheck roleCheck;

    // 매장 소유자 검증
    private final OwnerCheck ownerCheck;

    // 배송지 변경 ( 주문자 본인만 가능)
    public void changeDeliveryInfo(
            UUID orderId,
            String address,
            String detailAddress,
            String memo,
            Long userId
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getOrderer().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        order.changeDeliveryInfo(address, detailAddress, memo);
    }

    // 주문 생성
    public UUID createOrder(OrderServiceDto.Create dto, Long userId) {

        if (!roleCheck.hasRole("USER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_ITEM_EMPTY);
        }

        // 가게 존재 여부 검증
        Store store = productProvider.getStore(dto.getStoreId());

        // DTO → OrderItem 변환 + 메뉴/옵션 검증
        List<OrderItem> items = dto.getItems().stream()
                .map(item -> {

                    Menu menu = productProvider.getMenu(dto.getStoreId(), item.getMenuId());

                    orderCheck.validateMenu(
                            menu,
                            dto.getStoreId(),
                            item.getMenuPrice()
                    );

                    if (item.getOptions() != null && !item.getOptions().isEmpty()) {

                        List<String> optionNames = item.getOptions().stream()
                                .map(OrderServiceDto.Option::getName)
                                .toList();

                        optionCheck.validate(menu, optionNames);
                    }

                    return toOrderItem(item);
                })
                .toList();

        // Store 정보 스냅샷
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

        if (item.getMenuPrice() <= 0) {
            throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
        }

        List<OrderServiceDto.Option> options =
                item.getOptions() == null ? Collections.emptyList() : item.getOptions();

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
    public void cancelOrder(UUID orderId, Long userId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 관리자(MASTER, MANAGER)가 아니면서 주문자도 아닌 경우 차단
        if (!roleCheck.hasRole(List.of("MASTER", "MANAGER")) //(권한: 주문자 본인 또는 관리자 가능)
                && !order.getOrderer().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        order.cancel();
    }

    // 주문 상태 변경
    public void changeOrderStatus(UUID orderId, OrderStatus status) {

        // 1. OWNER 역할 확인
        if (!roleCheck.hasRole("OWNER")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 본인 매장 주문인지 확인
        if (!ownerCheck.isOwner(order.getStoreInfo().getStoreId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

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