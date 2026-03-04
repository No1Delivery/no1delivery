package com.sparta.no1delivery.domain.review.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.query.Order;
import org.springframework.data.annotation.Id;
import java.util.UUID;

/**
 * 리뷰는
 */
@Entity
@Table(name = "p_review")
public class Review extends BaseUserEntity {

    @Id
    private UUID review_id;  // 리뷰 id

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Long reviewer_id; // 리뷰 작성자 id
    private Integer rating; // 작성자 이름
    private String comment; // 리뷰 내용

    private String  reviewwe_name; // 리뷰 작성자 이름
    private UUID store_id; // 가게id
    private String store_name; // 가게 이름
    private UUID order_id; // 주문 id


}
