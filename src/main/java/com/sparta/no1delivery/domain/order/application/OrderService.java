package com.sparta.no1delivery.domain.order.application;

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

    // 주문 생성
    // TODO: 메뉴 검증
    // TODO: 가게 검증
    // TODO: 결제 요청
    // TODO: 주문 이벤트 발행
    public UUID createOrder(OrderServiceDto.Create dto, Long userId) {

        // OrderItem 변환
        List<OrderItem> items = dto.getItems() == null ? List.of() :
                dto.getItems().stream()
                        .map(item -> new OrderItem(
                                item.getMenuId(),
                                item.getMenuName(),
                                item.getMenuOption(),
                                item.getQuantity(),
                                item.getMenuPrice()
                        ))
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
                dto.getDeliveryMemo(),
                dto.getPhone()
        );

        // Order 생성
        Order order = Order.createOrder(
                userId,
                dto.getOrdererName(),
                storeInfo,
                deliveryInfo,
                items
        );

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getOrderId();
    }

    // 주문 취소
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 주문 취소 (취소 가능 여부 검증은 Order 엔티티에서 처리)
        order.cancel();
    }
}