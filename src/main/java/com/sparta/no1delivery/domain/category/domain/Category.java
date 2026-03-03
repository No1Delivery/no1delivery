package com.sparta.no1delivery.domain.category.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseUserEntity {

    @EmbeddedId
    private CategoryId id;

    @Column(nullable = false)
    private String name; // 카테고리명

    @Column(name = "display_order", nullable = false)
    private long displayOrder;


    @Builder
    public Category(UUID categoryId, String name, long displayOrder ,RoleCheck roleCheck) {
        // 권한 체크
        checkPossible(roleCheck);

        // 필드 초기화
        this.id = categoryId == null ? CategoryId.of():CategoryId.of(categoryId);
        this.name = name;
        this.displayOrder = displayOrder;
    }

    private void checkPossible(RoleCheck roleCheck) {
        if (!roleCheck.hasRole(List.of("MANAGER", "MASTER"))) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public Category changeName(String name, RoleCheck roleCheck) {
        return new Category(id.getId(), name, displayOrder, roleCheck);
    }
}