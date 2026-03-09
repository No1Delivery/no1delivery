package com.sparta.no1delivery.domain.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuId implements Serializable{

    @Column(length = 45, name="menu_id")
    private UUID id;

    public static MenuId of() {
        return MenuId.of(UUID.randomUUID());
    }

    public static MenuId of(UUID id) {
        return new MenuId(id);
    }

}