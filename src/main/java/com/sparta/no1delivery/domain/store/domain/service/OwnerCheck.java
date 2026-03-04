package com.sparta.no1delivery.domain.store.domain.service;

import java.util.UUID;

public interface OwnerCheck {
    boolean isOwner(UUID storeId);
    Long getOwnerId(); // 매장 주인 로그인 ID
    String getOwnerName(); // 매장 주인명
    UUID getStoreId();
}