package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.domain.store.domain.QStore;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.global.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityOwnerCheck implements OwnerCheck {

    private final StoreRepository storeRepository;
    private final UserDetails userDetails;


    @Override
    public boolean isOwner(UUID storeId) {
        if (storeId == null) return false;

        Long ownerId = userDetails.getId();
        if (ownerId == null || !userDetails.isAuthenticated()) {
            return false;
        }

        QStore store = QStore.store;
        return storeRepository.exists(
                store.id.id.eq(storeId)
                        .and(store.owner.id.eq(ownerId))
        );
    }

    @Override
    public Long getOwnerId() {
        return userDetails.getId();
    }

    @Override
    public String getOwnerName() {
        return userDetails.getName();
    }

}
