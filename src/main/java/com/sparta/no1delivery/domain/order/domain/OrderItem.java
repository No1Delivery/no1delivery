package com.sparta.no1delivery.domain.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "P_ORDER_ITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
public class OrderItem {

    // 주문 상품 PK
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID orderIdx;

    // 주문(Order)과 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 주문 시점의 메뉴 ID (스냅샷)
    @Column(nullable = false)
    private UUID menuId;

    // 주문 시점의 메뉴 이름 (스냅샷)
    @Column(nullable = false)
    private String menuName;

    // 사용자가 선택한 옵션 정보 (JSON 형태로 저장)
    @Column(columnDefinition = "json", nullable = true)
    private String menuOption;

    // 주문 수량
    @Column(nullable = false)
    private int quantity;

    // 주문 시점의 메뉴 가격 (스냅샷)
    @Column(nullable = false)
    private int menuPrice;

    // 해당 주문 상품의 총 가격 (menuPrice * quantity)
    @Column(nullable = false)
    private int subtotalPrice;

    // 주문 상품 생성
    public OrderItem(UUID menuId,
                     String menuName,
                     String menuOption,
                     int quantity,
                     int menuPrice,
                     int optionPrice) {

        // 메뉴 이름 검증 (주문 기록 보호)
        if (menuName == null || menuName.isBlank()) {
            throw new IllegalArgumentException("메뉴 이름은 필수입니다.");
        }

        // 수량 검증 (1개 이상)
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        // 가격 검증
        if (menuPrice < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }

        this.menuId = menuId;
        this.menuName = menuName;
        this.menuOption = menuOption;
        this.quantity = quantity;
        this.menuPrice = menuPrice;

        // 주문 상품 총 가격 계산
        this.subtotalPrice = (menuPrice + optionPrice) * quantity;
    }

    // Order와의 연관관계 설정
    void setOrder(Order order) {
        this.order = order;
    }
}