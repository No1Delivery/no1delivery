package com.sparta.no1delivery.domain.store.domain.query;

import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.query.dto.StoreQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StoreQueryRepository {

    Optional<Store> findById(StoreId id);
    Page<Store> findAll(StoreQueryDto.Search search, Pageable pageable);

}