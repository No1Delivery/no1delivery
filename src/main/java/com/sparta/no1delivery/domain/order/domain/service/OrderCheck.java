package com.sparta.no1delivery.domain.order.domain.service;

import com.sparta.no1delivery.domain.store.domain.Menu;
import java.util.UUID;

// 주문 가능 여부 및 주문 소유 여부 검증 서비스
public interface OrderCheck {

    void validateMenu(Menu menu, UUID storeId, Integer requestPrice);

}