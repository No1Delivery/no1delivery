package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.OrderService;
import com.sparta.no1delivery.domain.order.application.query.OrderQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    // 주문 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto.Create createOrder(
            @RequestBody @Valid OrderRequestDto request,
            @RequestParam Long userId
    ) {
        UUID orderId = orderService.createOrder(
                request.toServiceDto(),
                userId
        );

        return new OrderResponseDto.Create(orderId);
    }

    // 주문 취소 (주문 생성 후 5분 이내 가능)
    @PatchMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public OrderResponseDto.OrderDetail getOrderDetail(
            @PathVariable UUID orderId
    ) {
        return orderQueryService.getOrderDetail(orderId);
    }

    // 내 주문 목록 조회
    @GetMapping
    public Page<OrderResponseDto.Order> getMyOrders(
            @RequestParam Long userId,
            Pageable pageable
    ) {
        return orderQueryService.getUserOrders(userId, pageable);
    }

    // 주문 상태 조회
    @GetMapping("/{orderId}/status")
    public OrderResponseDto.OrderStatus getOrderStatus(
            @PathVariable UUID orderId
    ) {
        return orderQueryService.getOrderStatus(orderId);
    }
}