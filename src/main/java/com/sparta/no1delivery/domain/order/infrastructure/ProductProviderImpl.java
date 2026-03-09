package com.sparta.no1delivery.domain.order.infrastructure;

import com.sparta.no1delivery.domain.order.domain.OrderItem;
import com.sparta.no1delivery.domain.order.domain.service.ProductProvider;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.query.MenuQueryRepository;
import com.sparta.no1delivery.domain.store.domain.query.StoreQueryRepository;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductProviderImpl implements ProductProvider {

    private final StoreQueryRepository storeRepository;
    private final MenuQueryRepository menuRepository;

    // 가게 조회
    @Override
    public Store getStore(UUID storeId) {

        return storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() ->
                        new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    // 메뉴 조회
    @Override
    public Menu getMenu(UUID storeId, UUID menuId) {

        return menuRepository.findById(
                StoreId.of(storeId),
                MenuId.of(menuId)
        ).orElseThrow(() ->
                new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    // 주문 상품 반환 (현재는 그대로 사용)
    @Override
    public OrderItem getProduct(OrderItem item) {
        return item;
    }
}