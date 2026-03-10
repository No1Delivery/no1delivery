package com.sparta.no1delivery.domain.review.domain.service;

import java.util.UUID;

public interface StoreRatingCalculator {
    double getAverageRating(UUID storeId);
}
