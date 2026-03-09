package com.sparta.no1delivery.domain.order.domain.service;

import com.sparta.no1delivery.domain.order.domain.OrderItem;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.Store;

import java.util.UUID;

// 주문에서 사용할 상품(가게, 메뉴) 조회 서비스
public interface ProductProvider {

    // 가게 조회
    Store getStore(UUID storeId);

    // 메뉴 조회
    Menu getMenu(UUID storeId, UUID menuId);

    // 주문 상품 반환 (스냅샷용)
    OrderItem getProduct(OrderItem item);
}