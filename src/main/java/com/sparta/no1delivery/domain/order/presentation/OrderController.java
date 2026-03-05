package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.OrderService;
import com.sparta.no1delivery.domain.order.application.query.OrderQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

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

    @PatchMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto.OrderDetail getOrderDetail(
            @PathVariable UUID orderId
    ) {
        return orderQueryService.getOrderDetail(orderId);
    }
}