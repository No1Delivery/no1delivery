package com.sparta.no1delivery.domain.store.domain.query;

import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.query.dto.MenuQueryDto;

import java.util.List;
import java.util.Optional;

public interface MenuQueryRepository {

    Optional<Menu> findById(StoreId storeId, MenuId menuId);
    List<Menu> findAll(StoreId storeId, MenuQueryDto.Search search);

}
