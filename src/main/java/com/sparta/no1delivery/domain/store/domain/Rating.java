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
    private Integer count = 0;

    public void addRating(double newRating) {
        this.average = (average * count + newRating) / (count + 1);
        this.count++;
    }

    public void updateRating(double oldRating, double newRating) {
        if (count == null || count == 0) return;
        this.average = (average * count - oldRating + newRating) / count;
    }

    public void removeRating(double ratingToRemove) {
        if (count == null || count <= 1) {
            this.average = 0.0;
            this.count = 0;
        } else {
            this.average = (average * count - ratingToRemove) / count;
            this.count--;
        }
    }
}
