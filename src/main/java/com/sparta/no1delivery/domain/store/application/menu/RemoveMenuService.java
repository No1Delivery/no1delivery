package com.sparta.no1delivery.domain.store.application.menu;

import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoveMenuService {
    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final UserDetails userDetails;

    @Transactional
    public void removeMenu(UUID storeId, UUID menuId) {
        Store store = MenuServiceHelper.getStore(storeId, storeRepository);
        store.removeMenu(roleCheck, ownerCheck, MenuId.of(menuId), userDetails);

        log.info("Product removed from store {}: {}", storeId, menuId);
    }
}
