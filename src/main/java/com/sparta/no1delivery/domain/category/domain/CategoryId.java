package com.sparta.no1delivery.domain.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access =  AccessLevel.PROTECTED)
public class CategoryId {
    @Column(length = 45, name = "category_id")
    private UUID id;

    public static CategoryId of() {
        return CategoryId.of(UUID.randomUUID());
    }

    public static CategoryId of(UUID id) {
        return new CategoryId(id);
    }

}
