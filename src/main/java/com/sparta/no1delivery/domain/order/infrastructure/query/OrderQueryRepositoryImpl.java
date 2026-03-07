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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> findById(UUID orderId) {
        QOrder order = QOrder.order;

        Order result = queryFactory
                .selectFrom(order)
                .where(order.orderId.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<Order> findAll(OrderQueryDto.Search search, Pageable pageable) {

        QOrder order = QOrder.order;
        BooleanBuilder builder = new BooleanBuilder();

        if (search != null && search.getOrderIds() != null && !search.getOrderIds().isEmpty()) {
            builder.and(order.orderId.in(search.getOrderIds()));
        }

        List<Order> content = queryFactory
                .selectFrom(order)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Order> findAllByStore(UUID storeId, OrderQueryDto.Search search, Pageable pageable) {

        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(order.storeInfo.storeId.eq(storeId));

        List<Order> content = queryFactory
                .selectFrom(order)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Order> findAllByUser(Long userId, OrderQueryDto.Search search, Pageable pageable) {

        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(order.orderer.userId.eq(userId));

        List<Order> content = queryFactory
                .selectFrom(order)
                .where(builder)
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