package com.sparta.no1delivery.domain.order.domain.query;

import com.sparta.no1delivery.domain.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

// 주문 조회 전용 Repository
public interface OrderQueryRepository {

    // 주문 상세 조회
    Optional<Order> findById(UUID orderId);

    // 가게별 주문 목록 조회
    Page<Order> findAllByStore(UUID storeId, OrderQueryDto.Search search, Pageable pageable);

    // 사용자별 주문 목록 조회
    Page<Order> findAllByUser(Long userId, OrderQueryDto.Search search, Pageable pageable);

    // 전체 주문 조회
    Page<Order> findAll(OrderQueryDto.Search search, Pageable pageable);
}