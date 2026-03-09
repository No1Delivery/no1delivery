package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.OrderService;
import com.sparta.no1delivery.domain.order.application.query.OrderQueryService;
import com.sparta.no1delivery.domain.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "주문 API", description = "주문 생성, 조회, 취소, 상태 변경 관련 API")
public class OrderController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    // 주문 생성
    @Operation(summary = "주문 생성", description = "사용자가 새로운 주문을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto.Create createOrder(
            @RequestBody @Valid OrderRequestDto request,
            @Parameter(description = "주문한 사용자 ID")
            @RequestParam Long userId
    ) {
        UUID orderId = orderService.createOrder(
                request.toServiceDto(),
                userId
        );

        return new OrderResponseDto.Create(orderId);
    }

    // 배송지 변경
    @Operation(summary = "배송지 변경", description = "주문 접수 전까지만 배송지 정보를 변경할 수 있습니다.")
    @PatchMapping("/{orderId}/delivery-info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeDeliveryInfo(
            @Parameter(description = "주문 ID")
            @PathVariable UUID orderId,
            @RequestBody @Valid OrderRequestDto.ChangeDelivery request
    ) {
        orderService.changeDeliveryInfo(
                orderId,
                request.getAddress(),
                request.getDetailAddress(),
                request.getMemo()
        );
    }

    // 주문 취소
    @Operation(summary = "주문 취소", description = "주문 생성 후 5분 이내에 주문을 취소할 수 있습니다.")
    @PatchMapping("/{orderId}/cancel")
    public void cancelOrder(
            @Parameter(description = "주문 ID")
            @PathVariable UUID orderId
    ) {
        orderService.cancelOrder(orderId);
    }

    // 주문 상태 변경
    @Operation(summary = "주문 상태 변경", description = "주문의 상태를 변경합니다.")
    @PatchMapping("/{orderId}/status")
    public void changeOrderStatus(
            @Parameter(description = "주문 ID")
            @PathVariable UUID orderId,

            @Parameter(description = "변경할 주문 상태")
            @RequestParam OrderStatus status
    ) {
        orderService.changeOrderStatus(orderId, status);
    }

    // 주문 상세 조회
    @Operation(summary = "주문 상세 조회", description = "주문 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}")
    public OrderResponseDto.OrderDetail getOrderDetail(
            @Parameter(description = "주문 ID")
            @PathVariable UUID orderId
    ) {
        return orderQueryService.getOrderDetail(orderId);
    }

    // 주문 상태 조회
    @Operation(summary = "주문 상태 조회", description = "현재 주문 상태를 조회합니다.")
    @GetMapping("/{orderId}/status")
    public OrderResponseDto.OrderStatus getOrderStatus(
            @Parameter(description = "주문 ID")
            @PathVariable UUID orderId
    ) {
        return orderQueryService.getOrderStatus(orderId);
    }

    // 내 주문 목록 조회
    @Operation(summary = "내 주문 목록 조회", description = "사용자의 주문 목록을 조회합니다.")
    @GetMapping("/my")
    public Page<OrderResponseDto.Order> getMyOrders(
            @Parameter(description = "사용자 ID")
            @RequestParam Long userId,
            OrderRequestDto.Search search,
            Pageable pageable
    ) {
        return orderQueryService.getUserOrders(userId, search, pageable);
    }

    // 매장 주문 목록 조회
    @Operation(summary = "매장 주문 목록 조회", description = "특정 매장의 주문 목록을 조회합니다.")
    @GetMapping("/store/{storeId}")
    public Page<OrderResponseDto.Order> getStoreOrders(
            @PathVariable UUID storeId,
            OrderRequestDto.Search search,
            Pageable pageable
    ) {
        return orderQueryService.getStoreOrders(storeId, search, pageable);
    }

    // 전체 주문 조회 (관리자)
    @Operation(summary = "전체 주문 목록 조회", description = "관리자가 전체 주문을 조회합니다.")
    @GetMapping
    public Page<OrderResponseDto.Order> getOrders(
            OrderRequestDto.Search search,
            Pageable pageable
    ) {
        return orderQueryService.searchOrders(search, pageable);
    }
}