package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.OrderService;
import com.sparta.no1delivery.domain.order.application.query.OrderQueryService;
import com.sparta.no1delivery.domain.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
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

    // 내 주문 목록 조회
    @Operation(summary = "내 주문 목록 조회", description = "사용자의 주문 목록을 조회합니다.")
    @GetMapping
    public Page<OrderResponseDto.Order> getMyOrders(
            @Parameter(description = "사용자 ID")
            @RequestParam Long userId,
            OrderRequestDto.Search search,
            Pageable pageable
    ) {
        return orderQueryService.getUserOrders(userId, search, pageable);
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

    // 관리자 주문 검색
    @Operation(summary = "주문 검색", description = "관리자가 주문을 검색합니다.")
    @GetMapping("/search")
    public Page<OrderResponseDto.Order> searchOrders(
            OrderRequestDto.Search search,
            Pageable pageable
    ) {
        return orderQueryService.searchOrders(search, pageable);
    }
}