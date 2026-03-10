package com.sparta.no1delivery.domain.review.domain.event;

import java.util.UUID;

public record ReviewScoreChangedEvent(
        UUID storeId,
        Long reviewCount,
        double averageScore
) {
}
