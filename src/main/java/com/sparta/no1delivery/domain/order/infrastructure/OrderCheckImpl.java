package com.sparta.no1delivery.domain.order.infrastructure;

import com.sparta.no1delivery.domain.order.domain.service.OrderCheck;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderCheckImpl implements OrderCheck {

    @Override
    public void validateMenu(Menu menu, UUID storeId, Integer requestPrice) {

        // 메뉴가 해당 가게 메뉴인지 검증
        if (!menu.getStoreId().getId().equals(storeId)) {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }

        // 가격 위조 검증
        if (menu.getPrice().getValue() != requestPrice) {
            throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
        }
    }
}