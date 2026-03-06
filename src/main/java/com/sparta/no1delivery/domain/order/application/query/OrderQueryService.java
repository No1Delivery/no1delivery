package com.sparta.no1delivery.domain.order.application.query;

import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.query.OrderQueryRepository;
import com.sparta.no1delivery.domain.order.presentation.OrderRequestDto;
import com.sparta.no1delivery.domain.order.presentation.OrderResponseDto;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    // 주문 조회용 Repository
    private final OrderQueryRepository orderQueryRepository;

    //주문 상세 조회
    public OrderResponseDto.OrderDetail getOrderDetail(UUID orderId) {

        Order order = orderQueryRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return toDetailResponse(order);
    }

    //사용자 기준 주문 목록 조회 (내 주문)
    public Page<OrderResponseDto.Order> getUserOrders(
            Long userId,
            OrderRequestDto.Search search,
            Pageable pageable
    ) {

        return orderQueryRepository
                .findAllByUser(
                        userId,
                        search != null ? search.toQuerySearch() : null,
                        pageable
                )
                .map(this::toOrderResponse);
    }

    //관리자 주문 검색 (필터 조회)
    public Page<OrderResponseDto.Order> searchOrders(
            OrderRequestDto.Search search,
            Pageable pageable
    ) {

        return orderQueryRepository
                .findAll(
                        search != null ? search.toQuerySearch() : null,
                        pageable
                )
                .map(this::toOrderResponse);
    }

    //주문 상태 조회
    public OrderResponseDto.OrderStatus getOrderStatus(UUID orderId) {

        Order order = orderQueryRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return OrderResponseDto.OrderStatus.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .build();
    }

    //주문 목록 조회용 DTO 변환
    private OrderResponseDto.Order toOrderResponse(Order order) {

        return OrderResponseDto.Order.builder()
                .orderId(order.getOrderId())
                .storeName(order.getStoreInfo().getStoreName())
                .totalOrderPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }

    ///주문 상세 조회용 DTO 변환
    private OrderResponseDto.OrderDetail toDetailResponse(Order order) {

        return OrderResponseDto.OrderDetail.builder()
                .orderId(order.getOrderId())
                .storeName(order.getStoreInfo().getStoreName())
                .ordererName(order.getOrderer().getName())
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