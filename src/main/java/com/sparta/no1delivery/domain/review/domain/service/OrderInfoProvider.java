package com.sparta.no1delivery.domain.review.domain.service;

import com.sparta.no1delivery.domain.review.domain.ReviewOrderInfo;

import java.util.UUID;

public interface OrderInfoProvider {
    ReviewOrderInfo getOrderInfo(UUID orderId);
}
