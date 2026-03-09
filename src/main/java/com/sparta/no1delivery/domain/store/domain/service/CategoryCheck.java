package com.sparta.no1delivery.domain.store.domain.service;

import java.util.List;
import java.util.UUID;

public interface CategoryCheck {
    boolean exists(List<UUID> categoryIds); // 매장에 카테고리 등록,수정 요청시 모든 카테고리가 존재하는지 체크
}