package com.sparta.no1delivery.domain.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.util.UUID;

@Embeddable
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewId {

    @Column(length = 45, name = "review_id")
    private UUID id;

    // 새 리뷰를 처음 만들 때
    public static ReviewId of() {
        return ReviewId.of(UUID.randomUUID());
    }

    // 이미 있는 UUID를 받아서 ReviewId를 생성
    public static ReviewId of(UUID id) {
        return new ReviewId(id);
    }
}
