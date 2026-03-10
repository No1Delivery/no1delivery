package com.sparta.no1delivery.domain.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rating {

    @Column(name = "average_rating")
    private Double average = 0.0;

    @Column(name = "review_count")
    private Long count = 0L;

    public void updateRating(Double average, Long count) {
        this.average = average;
        this.count = count;
    }
}
