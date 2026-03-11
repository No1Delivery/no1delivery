package com.sparta.no1delivery.domain.order.domain;

import com.sparta.no1delivery.domain.order.infrastructure.converter.SelectedOptionConverter;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "P_ORDER_ITEM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    // 선택된 옵션 정보 (JSON 저장)
    @Convert(converter = SelectedOptionConverter.class)
    @Column(name = "options", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<SelectedOption> selectedOptions;

    // 주문 수량
    @Column(nullable = false)
    private int quantity;

    // 주문 시점의 메뉴 가격 (스냅샷)
    @Column(nullable = false)
    private int menuPrice;

    // 주문 상품 총 가격
    @Column(nullable = false)
    private int subtotalPrice;


    // OrderItem 생성
    public OrderItem(UUID menuId,
                     String menuName,
                     List<SelectedOption> selectedOptions,
                     int quantity,
                     int menuPrice) {

        // 메뉴 이름 검증
        if (menuName == null || menuName.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 수량 검증
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 가격 검증
        if (menuPrice < 0) {
            throw new CustomException(ErrorCode.INVALID_MENU_PRICE);
        }

        this.menuId = menuId;
        this.menuName = menuName;
        this.selectedOptions = selectedOptions;
        this.quantity = quantity;
        this.menuPrice = menuPrice;

        // 옵션 가격 계산
        int optionPrice = calculateOptionPrice(selectedOptions);

        // 주문 상품 총 가격 계산
        this.subtotalPrice = (menuPrice + optionPrice) * quantity;
    }


    // 옵션 가격 계산
    private int calculateOptionPrice(List<SelectedOption> options) {

        if (options == null || options.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (SelectedOption option : options) {

            int subTotal = option.getSubOptions() == null ? 0 :
                    option.getSubOptions().stream()
                            .mapToInt(SelectedOption.SelectedSubOption::getAddPrice)
                            .sum();

            total += option.getOptionPrice() + subTotal;
        }

        return total;
    }


    // Order 연관관계 설정
    void setOrder(Order order) {
        this.order = order;
    }
}