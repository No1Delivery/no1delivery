package com.sparta.no1delivery.domain.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,ReviewId> {
}
