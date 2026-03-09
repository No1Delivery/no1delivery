package com.sparta.no1delivery.global.domain.service;

import java.util.UUID;

public interface OwnerCheck {
    boolean isOwner(UUID storeId);
    Long getOwnerId(); // 매장 주인 UserId
    String getOwnerName(); // 매장 주인명
    UUID getStoreId();
}