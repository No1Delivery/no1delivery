package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.OrderService;
import com.sparta.no1delivery.domain.order.application.dto.OrderServiceDto;
import com.sparta.no1delivery.domain.order.application.query.OrderQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
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

        OrderServiceDto.Create dto = OrderServiceDto.Create.builder()
                .ordererName(request.getOrdererName())
                .storeId(request.getStoreId())
                .storeName(request.getStoreName())
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryAddressDetail(request.getDeliveryAddressDetail())
                .deliveryMemo(request.getDeliveryMemo())
                .phone(request.getPhone())
                .items(
                        request.getItems().stream()
                                .map(item -> OrderServiceDto.Item.builder()
                                        .menuId(item.getMenuId())
                                        .menuName(item.getMenuName())
                                        .menuOption(item.getMenuOption())
                                        .quantity(item.getQuantity())
                                        .menuPrice(item.getMenuPrice())
                                        .build())
                                .toList()
                )
                .build();

        UUID orderId = orderService.createOrder(dto, userId);

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