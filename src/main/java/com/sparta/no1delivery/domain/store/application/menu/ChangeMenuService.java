package com.sparta.no1delivery.domain.store.application.menu;

import com.sparta.no1delivery.domain.store.application.dto.StoreServiceDto;
import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.MenuStatus;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeMenuService {
    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;

    @Transactional
    public void updateMenu(UUID storeId, UUID menuId, StoreServiceDto.Menu dto) {
        Store store = MenuServiceHelper.getStore(storeId, storeRepository);
        store.updateMenu(MenuId.of(menuId), MenuServiceHelper.toMenu(roleCheck, ownerCheck, dto));
    }

    @Transactional
    public void changeMenuStatus(UUID storeId, UUID menuId, String status) {
        Store store = MenuServiceHelper.getStore(storeId, storeRepository);
        store.changeMenuStatus(roleCheck, ownerCheck, MenuId.of(menuId), MenuStatus.from(status));
    }
}
