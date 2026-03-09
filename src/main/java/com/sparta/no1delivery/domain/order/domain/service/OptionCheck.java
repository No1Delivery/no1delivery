package com.sparta.no1delivery.domain.order.domain.service;

import com.sparta.no1delivery.domain.store.domain.Menu;
import java.util.List;

//주문 상품 옵션 검증 서비스
public interface OptionCheck {

    void validate(Menu menu, List<String> optionNames);
}