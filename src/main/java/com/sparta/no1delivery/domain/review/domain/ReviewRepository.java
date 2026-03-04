package com.sparta.no1delivery.domain.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review,ReviewId> {
    // 특정 주문에 대한 리뷰가 이미 존재하는지 확인
    Optional<Review> findByOrder_OrderId(UUID orderId);

    // 작성자(userId)가 작성한 리뷰 목록 조회
    List<Review> findAllByReviewerId(Long reviewerId);
}
