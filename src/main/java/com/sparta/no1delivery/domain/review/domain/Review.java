package com.sparta.no1delivery.domain.review.domain;

import com.sparta.no1delivery.domain.review.domain.service.OrderInfoProvider;
import com.sparta.no1delivery.domain.review.domain.service.ReviewerCheck;
import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.domain.RoleCheck;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

/**
 * 1. 리뷰 작성 시 주문자와 로그인한 사용자가 같은지 체크
 * 2. 리뷰 수정인 경우 리뷰 작성자가 로그인한 사용자와 같은지도 체크
 * (주문 번호는 최초 등록시에만 수정이 되므로 수정일 땐 체크 불필요)
 * 관리자 (MANAGER, MASTER)는 권한 체크 필요없이 항상 가능 (추가, 수정)
 * 3. 리뷰는 주문 상태가 완료 (DELIVERED)로 변경이 되면(배달 완료 상태) 리뷰를 작성 할 수 있음.
 * 4. 리뷰는 주문을 한 사용자만 작성 가능
 * 5. 리뷰의 평점은 필수이며 1~5점 사이 선택
 * 6. 리뷰 작성 또는 수정이 완료되면 평점에 대한 평균을 주문에 해당하는 상점의 평점에 업데이트 (이벤트 발행)
 * 7. 하나의 주문, 하나의 리뷰를 작성하는 원칙
 */

@Entity
@ToString
@Access(AccessType.FIELD)
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="P_Review", indexes = {
        @Index(name = "idx_review_order_id", columnList = "order_id, deleted_at", unique = true)
})
public class Review extends BaseUserEntity {

    @EmbeddedId
    private ReviewId reviewId;

    @Embedded
    private Reviewer reviewer;

    @Embedded
    private ReviewOrderInfo info; // 주문 정보

    @Embedded
    private ReviewContent content; // 리뷰 내용

    @Builder
    public Review(UUID orderId, String subject, String content, int score, OrderInfoProvider orderInfoProvider, RoleCheck roleCheck,
                  ReviewerCheck reviewerCheck, UserDetails userDetails) {
        // 권한 체크


        // 작성 가능한 리뷰인지 체크 - 주문 존재 여부 및 상태 확인(DELIVERED)
    }

    // 리뷰 수정
    public void change() {
        // 권한 체크

    }

    // 리뷰 삭제 (Soft Delete)
    public void remove() {
        // 권한 체크

    }

    /**
     * 1. 리뷰 작성 시 주문자와 로그인한 가용자가 같은지 체크
     * 2. 리뷰 수정인 경우 리뷰 작성자가 로그인한 사용자와 같은지도 체크
     * (주문 번호는 최초 등록 시에만 수정이 되므로 수정일 땐 체크 불필요)
     * 3. 관리자 (MANAGER, MASTER)는 권한 체크 필요없이 항상 가능
     */
    private void checkAuthority() {

    }
}