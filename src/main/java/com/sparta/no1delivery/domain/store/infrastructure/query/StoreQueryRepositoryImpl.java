package com.sparta.no1delivery.domain.store.infrastructure.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.StoreStatus;
import com.sparta.no1delivery.domain.store.domain.query.StoreQueryRepository;
import com.sparta.no1delivery.domain.store.domain.query.dto.StoreQueryDto;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.no1delivery.domain.store.domain.QMenu.menu;
import static com.sparta.no1delivery.domain.store.domain.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;

    @Override
    public Optional<Store> findById(StoreId id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(store)
                        .where(
                                store.id.eq(id).and(store.deletedAt.isNull()),
                                canAccessDefunct()
                        )
                        .fetchOne()
        );
    }

    @Override
    public Page<Store> findAll(StoreQueryDto.Search search, Pageable pageable) {
        NumberExpression<Double> distanceExpression = getDistanceExpression(search);

        // 데이터 조회 쿼리
        List<Store> storeList = queryFactory
                .selectFrom(store)
                .leftJoin(store.menus, menu)
                .where(
                        categoryIn(search.getCategoryIds()),
                        statusFilter(search),
                        keywordContains(search.getKeyword()),
                        distanceWithin(distanceExpression, search)
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, distanceExpression))
                .fetch();

        // 카운트 쿼리
        Long total = queryFactory
                .select(store.countDistinct())
                .from(store)
                .leftJoin(store.menus, menu)
                .where(
                        categoryIn(search.getCategoryIds()),
                        statusFilter(search),
                        keywordContains(search.getKeyword()),
                        distanceWithin(distanceExpression, search)
                )
                .fetchOne();

        return new PageImpl<>(storeList, pageable, total != null ? total : 0L);
    }

    private BooleanExpression canAccessDefunct() {
        if (roleCheck.hasRole(List.of("MANAGER", "MASTER"))) return null;

        if (roleCheck.hasRole("OWNER")) {
            return store.owner.id.eq(ownerCheck.getOwnerId())
                    .or(store.status.ne(StoreStatus.DEFUNCT));
        }
        return store.status.ne(StoreStatus.DEFUNCT);
    }

    private BooleanExpression categoryIn(List<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return null;
        }

        return store.categories.any().categoryId.in(categoryIds);
    }

    private BooleanBuilder statusFilter(StoreQueryDto.Search search) {
        BooleanBuilder builder = new BooleanBuilder();
        StoreStatus status = search.getStoreStatus();

        // MASTER & MANAGER
        if (roleCheck.hasRole(List.of("MANAGER", "MASTER"))) {
            if (status != null) {
                builder.and(store.status.eq(status));
            }
            return builder;
        }

        // OWNER
        if (roleCheck.hasRole("OWNER")) {
            if (Boolean.TRUE.equals(search.getOnlyMyStores())) {
                builder.and(store.owner.id.eq(ownerCheck.getOwnerId()));
                if (status != null) builder.and(store.status.eq(status));
                return builder;
            }

            if (status != null) {
                if (status == StoreStatus.DEFUNCT) {
                    builder.and(store.status.eq(StoreStatus.DEFUNCT))
                            .and(store.owner.id.eq(ownerCheck.getOwnerId()));
                } else {
                    builder.and(store.status.eq(status));
                }
            } else {
                builder.and(store.owner.id.eq(ownerCheck.getOwnerId()))
                        .or(store.status.ne(StoreStatus.DEFUNCT));
            }
            return builder;
        }

        // CUSTOMER
        builder.and(store.status.ne(StoreStatus.DEFUNCT));

        if (status == StoreStatus.OPEN) {
            builder.and(store.status.eq(StoreStatus.OPEN));
        }

        return builder;
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;

        return store.name.containsIgnoreCase(keyword)
                .or(menu.name.containsIgnoreCase(keyword));
    }

    private NumberExpression<Double> getDistanceExpression(StoreQueryDto.Search search) {
        if (search.getLatitude() == null || search.getLongitude() == null) return null;

        String userLocation = "POINT(%.10f %.10f)".formatted(search.getLongitude(), search.getLatitude());
        return Expressions.numberTemplate(Double.class,
                "ST_DistanceSphere({0}, ST_GeomFromText({1}, 4326))",
                store.address.point, userLocation);
    }

    private BooleanExpression distanceWithin(NumberExpression<Double> distanceExpression, StoreQueryDto.Search search) {
        if (!roleCheck.hasRole("CUSTOMER") || search.getLatitude() == null || search.getLongitude() == null) {
            return null;
        }

        double radiusMeter = (search.getRadiusKm() == null || search.getRadiusKm() < 0.0)
                                ? 3000.0 : search.getRadiusKm() * 1000;

        return distanceExpression.loe(radiusMeter);
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable, NumberExpression<Double> distanceExpression) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                switch (order.getProperty()) {
                    case "distance":
                        if (distanceExpression != null) {
                            orders.add(distanceExpression.asc());
                        }
                        break;
                    case "rating":
                        orders.add(store.rating.average.desc()); // 평점 높은 순
                        break;
                    case "review":
                        orders.add(store.rating.count.desc()); // 리뷰 많은 순
                        break;
                    default:
                        orders.add(store.createdAt.desc()); // 기본값 생성일순
                }
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

}
