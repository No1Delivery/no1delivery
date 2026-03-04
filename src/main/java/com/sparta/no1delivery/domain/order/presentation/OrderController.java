package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.OrderService;
import com.sparta.no1delivery.domain.order.application.query.OrderQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
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
        UUID orderId = orderService.createOrder(request, userId);

        return OrderResponseDto.Create.builder()
                .orderId(orderId)
                .status("ORDERED")
                .build();
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public OrderResponseDto.OrderDetail getOrderDetail(
            @PathVariable UUID orderId
    ) {
        return orderQueryService.getOrderDetail(orderId);
    }
}