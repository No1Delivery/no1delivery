package com.sparta.no1delivery.domain.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

// 주문 Command 전용 Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

}