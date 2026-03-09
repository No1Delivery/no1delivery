package com.sparta.no1delivery.domain.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "P_ORDER_ITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column( nullable = false, updatable = false)
    private UUID orderIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column( nullable = false)
    private UUID menuId;

    @Column( nullable = false)
    private String menuName;

    @Column( columnDefinition = "json" ,nullable = true)
    private String menuOption;

    @Column(nullable = false)
    private int quantity;

    @Column( nullable = false)
    private int menuPrice;

    @Column( nullable = false)
    private int subtotalPrice;

    // 주문 상품 생성
    public OrderItem(UUID menuId,
                     String menuName,
                     String menuOption,
                     int quantity,
                     int menuPrice) {

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
        this.subtotalPrice = menuPrice * quantity;
    }

    // 연관관계 설정 메서드 (Aggregate 내부에서만 호출되도록 접근 제한)
    void setOrder(Order order) {
        this.order = order;
    }
}