package com.sparta.no1delivery.domain.order.domain.query;

import com.sparta.no1delivery.domain.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

 // 주문 조회(Query) 전용 Repository
public interface OrderQueryRepository extends JpaRepository<Order, UUID> {

    // 유저별 주문 조회
    List<Order> findAllByOrdererId(Long ordererId);
    // 가게별 주문 조회
    List<Order> findAllByStoreInfo_StoreId(UUID storeId);
}