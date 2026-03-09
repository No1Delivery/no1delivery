package com.sparta.no1delivery.domain.order.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Orderer {

    private Long userId;
    private String name;
    private String phone;

    protected Orderer() {
    }

    public Orderer(Long userId, String name, String phone) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
    }
}