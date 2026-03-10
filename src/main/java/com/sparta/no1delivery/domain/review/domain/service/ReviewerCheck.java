package com.sparta.no1delivery.domain.review.domain.service;

import com.sparta.no1delivery.domain.review.domain.ReviewId;

import java.util.UUID;

/**
 * 1. 리뷰 작성 시 주문자와 로그인 한 사용자가 같은지 체크
 * 2. 리뷰 수정인 경우 리뷰 작성자가 로그인 한 사용자와 같은지도 체크
 * (주문 번호는 최초 등록시에만 수정이 되므로 수정일 땐 체크 불필요
 */
public interface ReviewerCheck {
    boolean check(ReviewId reviewId, UUID orderId);
}
