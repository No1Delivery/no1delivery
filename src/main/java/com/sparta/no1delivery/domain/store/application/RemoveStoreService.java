package com.sparta.no1delivery.domain.store.application;

import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveStoreService {

    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final UserDetails userDetails;

    @Transactional
    public void remove(UUID storeId) {
        Store store = storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        store.remove(roleCheck, ownerCheck, userDetails);
    }

}
