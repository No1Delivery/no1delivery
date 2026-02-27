package com.sparta.no1delivery.domain.category.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseUserEntity {

    @EmbeddedId
    private CategoryId id;

    private String name; // 카테고리명


    @Builder
    public Category(UUID categoryId, String name, RoleCheck roleCheck) {
        //권한 체크
        //checkPossible(roleCheck);

        this.id = categoryId == null ? CategoryId.of():CategoryId.of(categoryId);
        this.name = name;
    }

    /*private void checkPossible(RoleCheck roleCheck) {
        if (!roleCheck.hasRole(List.of("MANAGER", "MASTER"))) {
            throw new UnAuthorizedException();
        }
    }

    public Category changeName(String name, RoleCheck roleCheck) {

        return new Category(id.getId(), name, roleCheck);
    }*/
}