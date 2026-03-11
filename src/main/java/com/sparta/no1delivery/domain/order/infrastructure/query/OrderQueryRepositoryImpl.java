package com.sparta.no1delivery.domain.order.infrastructure.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.domain.order.domain.Order;
import com.sparta.no1delivery.domain.order.domain.QOrder;
import com.sparta.no1delivery.domain.order.domain.query.OrderQueryDto;
import com.sparta.no1delivery.domain.order.domain.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 주문 단건 조회
    @Override
    public Optional<Order> findById(UUID orderId) {

        QOrder order = QOrder.order;

        Order result = queryFactory
                .selectFrom(order)
                .where(order.orderId.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    // 관리자 주문 검색 (필터 조회)
    @Override
    public Page<Order> findAll(OrderQueryDto.Search search, Pageable pageable) {

        QOrder order = QOrder.order;
        BooleanBuilder builder = new BooleanBuilder();

        if (search != null) {

            if (search.getOrderIds() != null && !search.getOrderIds().isEmpty()) {
                builder.and(order.orderId.in(search.getOrderIds()));
            }

            if (search.getOrdererName() != null && !search.getOrdererName().isBlank()) {
                builder.and(order.orderer.name.containsIgnoreCase(search.getOrdererName()));
            }

            if (search.getStoreIds() != null && !search.getStoreIds().isEmpty()) {
                builder.and(order.storeInfo.storeId.in(search.getStoreIds()));
            }

            if (search.getStoreName() != null && !search.getStoreName().isBlank()) {
                builder.and(order.storeInfo.storeName.containsIgnoreCase(search.getStoreName()));
            }

            if (search.getDeliveryAddress() != null && !search.getDeliveryAddress().isBlank()) {
                builder.and(order.deliveryInfo.address.containsIgnoreCase(search.getDeliveryAddress()));
            }

            if (search.getOrderStatuses() != null && !search.getOrderStatuses().isEmpty()) {
                builder.and(order.status.stringValue().in(search.getOrderStatuses()));
            }
        }

        java.util.List<Order> content = queryFactory
                .selectFrom(order)
                .where(builder)
                .orderBy(order.createdAt.desc())   // 최신순 정렬 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 특정 매장의 주문 조회
    @Override
    public Page<Order> findAllByStore(UUID storeId, OrderQueryDto.Search search, Pageable pageable) {

        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(order.storeInfo.storeId.eq(storeId));

        java.util.List<Order> content = queryFactory
                .selectFrom(order)
                .where(builder)
                .orderBy(order.createdAt.desc())   // 최신순 정렬 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 특정 사용자의 주문 조회 (내 주문 목록)
    @Override
    public Page<Order> findAllByUser(Long userId, OrderQueryDto.Search search, Pageable pageable) {

        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(order.orderer.userId.eq(userId));

        java.util.List<Order> content = queryFactory
                .selectFrom(order)
                .where(builder)
                .orderBy(order.createdAt.desc())   // 최신순 정렬 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}