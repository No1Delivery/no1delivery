package com.sparta.no1delivery.domain.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner {

    @Column(length=45, name="owner_id", nullable = false)
    public Long id;

    @Column(length=45, name="owner_name", nullable = false)
    public String name;

    @Builder
    protected Owner(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
