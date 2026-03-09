package com.sparta.no1delivery.domain.store.infrastructure.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuId;
import com.sparta.no1delivery.domain.store.domain.MenuStatus;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.query.MenuQueryRepository;
import com.sparta.no1delivery.domain.store.domain.query.dto.MenuQueryDto;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sparta.no1delivery.domain.store.domain.QMenu.menu;

@Repository
@RequiredArgsConstructor
public class MenuQueryRepositoryImpl implements MenuQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;

    @Override
    public Optional<Menu> findById(StoreId storeId, MenuId menuId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(menu)
                        .where(
                                menu.id.eq(menuId),
                                menu.storeId.eq(storeId),
                                menu.deletedAt.isNull(),
                                statusFilter(storeId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<Menu> findAll(StoreId storeId, MenuQueryDto.Search search) {
        return queryFactory
                .selectFrom(menu)
                .where(
                        menu.storeId.eq(storeId),
                        nameContains(search.getKeyword()),
                        menu.deletedAt.isNull(),
                        statusFilter(storeId)
                )
                .orderBy(menu.createdAt.asc())
                .fetch();
    }

    private BooleanExpression nameContains(String keyword) {
        return (keyword != null && !keyword.isBlank())
                ? menu.name.containsIgnoreCase(keyword)
                : null;
    }

    // 고객은 HIDDEN 상태 메뉴 조회 불가
    private BooleanExpression statusFilter(StoreId storeId) {
        if (roleCheck.hasRole(List.of("MANAGER", "MASTER"))
                || (roleCheck.hasRole("OWNER") && ownerCheck.isOwner(storeId.getId()))) {
            return null;
        }

        return menu.status.ne(MenuStatus.HIDDEN);
    }
}
