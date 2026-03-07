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

    //TODO: Spring Security 적용 시 userId 제거
    //TODO: Swagger API 문서 어노테이션 추가

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

    // 내 주문 목록 조회(필터 조회)
    @GetMapping
    public Page<OrderResponseDto.Order> getMyOrders(
            @RequestParam Long userId,
            OrderRequestDto.Search search,
            Pageable pageable
    ) {
        return orderQueryService.getUserOrders(userId, search, pageable);
    }

    // 주문 상태 조회
    @GetMapping("/{orderId}/status")
    public OrderResponseDto.OrderStatus getOrderStatus(
            @PathVariable UUID orderId
    ) {
        return orderQueryService.getOrderStatus(orderId);
    }

    // 관리자 주문 검색 (필터 조회)
    @GetMapping("/search")
    public Page<OrderResponseDto.Order> searchOrders(
            OrderRequestDto.Search search,
            Pageable pageable
    ) {
        return orderQueryService.searchOrders(search, pageable);
    }
    // 주문 접수
    @PatchMapping("/{orderId}/accept")
    public void acceptOrder(@PathVariable UUID orderId) {
        orderService.acceptOrder(orderId);
    }

    // 조리 시작
    @PatchMapping("/{orderId}/preparing")
    public void startPreparing(@PathVariable UUID orderId) {
        orderService.startPreparing(orderId);
    }

    // 조리 완료
    @PatchMapping("/{orderId}/ready")
    public void readyOrder(@PathVariable UUID orderId) {
        orderService.readyOrder(orderId);
    }

    // 배송 시작
    @PatchMapping("/{orderId}/delivery")
    public void startDelivery(@PathVariable UUID orderId) {
        orderService.startDelivery(orderId);
    }

    // 배송 완료
    @PatchMapping("/{orderId}/delivery-done")
    public void deliveryDone(@PathVariable UUID orderId) {
        orderService.deliveryDone(orderId);
    }

    // 주문 최종 완료
    @PatchMapping("/{orderId}/complete")
    public void completeOrder(@PathVariable UUID orderId) {
        orderService.completeOrder(orderId);
    }
}