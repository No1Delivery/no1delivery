package com.sparta.no1delivery.domain.store.domain.query;

import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.query.dto.MenuQueryDto;

import java.util.List;
import java.util.Optional;

public interface MenuQueryRepository {

    Optional<Menu> findById(MenuId id);
    List<Menu> findAll(StoreId id, MenuQueryDto.Search search);

}
