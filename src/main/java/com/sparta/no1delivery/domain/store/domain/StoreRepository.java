package com.sparta.no1delivery.domain.store.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StoreRepository extends JpaRepository<Store, StoreId>, QuerydslPredicateExecutor<Store> {
}
