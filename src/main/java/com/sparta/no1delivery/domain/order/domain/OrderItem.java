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
    @Column(name = "order_idx", nullable = false, updatable = false)
    private UUID orderIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private UUID menuId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "menu_option", columnDefinition = "json" ,nullable = true)
    private String menuOption;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "menu_price", nullable = false)
    private int menuPrice;

    @Column(name = "subtotal_price", nullable = false)
    private int subtotalPrice;

    public OrderItem(UUID menuId,
                     String menuName,
                     String menuOption,
                     int quantity,
                     int menuPrice) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

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

    // Aggregate 내부에서만 호출되도록 접근 제한
    void setOrder(Order order) {
        this.order = order;
    }
}