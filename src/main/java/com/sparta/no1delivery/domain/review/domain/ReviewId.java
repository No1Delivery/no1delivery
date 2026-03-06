package com.sparta.no1delivery.domain.review.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ReviewId implements Serializable {
    private UUID value;

    protected ReviewId() {}

    public ReviewId(UUID value) {
        this.value = value;
    }

    public static ReviewId of() {
        return new ReviewId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }
}
