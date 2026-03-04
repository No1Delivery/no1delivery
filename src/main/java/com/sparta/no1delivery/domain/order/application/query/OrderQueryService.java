package com.sparta.no1delivery.domain.order.application.query;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.query.OrderQueryRepository;
import com.sparta.no1delivery.domain.order.presentation.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    public OrderResponseDto.OrderDetail getOrderDetail(UUID orderId) {
        Order order = orderQueryRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        return toDetailResponse(order);
    }

    public List<OrderResponseDto.Order> getUserOrders(Long userId) {
        return orderQueryRepository.findAllByOrdererId(userId)
                .stream()
                .map(this::toOrderResponse)
                .toList();
    }

    private OrderResponseDto.Order toOrderResponse(Order order) {
        return OrderResponseDto.Order.builder()
                .orderId(order.getOrderId())
                .storeName(order.getStoreInfo().getStoreName())
                .totalOrderPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderResponseDto.OrderDetail toDetailResponse(Order order) {
        return OrderResponseDto.OrderDetail.builder()
                .orderId(order.getOrderId())
                .storeName(order.getStoreInfo().getStoreName())
                .ordererName(order.getOrdererName())
                .deliveryAddress(order.getDeliveryInfo().getAddress())
                .deliveryMemo(order.getDeliveryInfo().getRequestMessage())
                .totalOrderPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .items(
                        order.getOrderItems().stream()
                                .map(item -> OrderResponseDto.OrderItem.builder()
                                        .itemName(item.getMenuName())
                                        .quantity(item.getQuantity())
                                        .price(item.getMenuPrice())
                                        .totalPrice(item.getSubtotalPrice())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}