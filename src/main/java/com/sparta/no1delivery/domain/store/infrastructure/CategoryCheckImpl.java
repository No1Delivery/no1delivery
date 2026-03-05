package com.sparta.no1delivery.domain.store.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.domain.category.domain.CategoryId;
import com.sparta.no1delivery.domain.category.domain.QCategory;
import com.sparta.no1delivery.domain.store.domain.service.CategoryCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryCheckImpl implements CategoryCheck {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean exists(List<UUID> categoryIds) {
        QCategory category = QCategory.category;

        if (categoryIds == null || categoryIds.isEmpty()) return true;

        // 중복 제거 & UUID -> CategoryId 변환
        List<CategoryId> distinctIds = categoryIds.stream()
                .distinct()
                .map(CategoryId::of)
                .toList();

        // 실제 DB에 있는 카테고리와 일치하는 개수
        long count = Objects.requireNonNullElse(queryFactory
                .select(category.count())
                .from(category)
                .where(category.id.in(distinctIds))
                .fetchOne(), 0L);

        return count == distinctIds.size();
    }
}
