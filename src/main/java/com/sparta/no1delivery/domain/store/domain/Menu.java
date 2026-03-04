package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.domain.Price;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

@Getter
@ToString
@Entity
@Table(name = "P_MENU", indexes = {
        @Index(name = "idx_menu_code", columnList = "menuCode")
})
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseUserEntity {

    @EmbeddedId
    private MenuId id;

    @Column(length = 45, unique = true, nullable = false)
    private String menuCode; // 메뉴 관리 코드

    private String name;

    private String description;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private MenuStatus status; // 메뉴 판매 상태

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "value", column = @Column(name = "price"))
    )
    private Price price;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_MENU_OPTION", joinColumns = {
            @JoinColumn(name = "store_id"),
            @JoinColumn(name = "menu_idx")
    })
    @OrderColumn(name = "option_idx")
    private List<MenuOption> options;

    @Builder
    protected Menu(StoreId storeId, int menuIdx, String menuCode, String name, String description, int price, List<MenuOption> options) {
        this.id = new MenuId(storeId, menuIdx);
        this.menuCode = StringUtils.hasText(menuCode) ? menuCode : UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = new Price(price);
        this.status = MenuStatus.SALE; // 기본값 판매중

        this.options = new ArrayList<>();
        if (options != null) {
            createOptions(options);
        }
    }

    // 메뉴 삭제
    public void markDeleted(Long userId) {
        deletedAt = LocalDateTime.now();
        deletedBy = userId;
    }

    // 옵션 여러개 등록
    public void createOptions(List<MenuOption> newOptions) {
        if (newOptions == null || newOptions.isEmpty()) return;
        this.options = Objects.requireNonNullElseGet(this.options, ArrayList::new); // 리스트 없으면 생성

        for (MenuOption newOption : newOptions) {
            validateDuplicateOption(newOption.getName());
        }

        this.options.addAll(newOptions);
    }

    // 옵션 하나 등록
    public void createOption(String name, int price, List<MenuSubOption> subOptions, boolean isEssential, boolean isMultiple) {
        MenuOption newOption = MenuOption.builder()
                .name(name)
                .price(price)
                .subOptions(subOptions)
                .isEssential(isEssential)
                .isMultiple(isMultiple)
                .build();

        createOptions(List.of(newOption));
    }

    // 옵션 여러개 삭제
    public void deleteOptions(List<Integer> indexes) {
        if (this.options == null || indexes == null) return;

        List<MenuOption> remainingOptions = IntStream.range(0, this.options.size())
                .filter(i -> !indexes.contains(i))
                .mapToObj(this.options::get)
                .toList();

        replaceOptions(remainingOptions);
    }

    // 옵션 하나 삭제
    public void deleteOption(int index) {
        if (options != null && index >= 0 && index < options.size()) {
            deleteOptions(List.of(index));
        }
    }

    // 옵션 비우기
    public void truncateOption() {
        if (options != null) {
            options.clear();
        }
    }

    // 옵션 교체
    public void replaceOptions(List<MenuOption> options) {
        truncateOption();
        createOptions(options);
    }

    // 옵션 중복 체크
    private void validateDuplicateOption(String name) {
        boolean isDuplicate = options.stream()
                .anyMatch(o -> o.getName().equals(name));

        if (isDuplicate) {
            throw new CustomException(ErrorCode.DUPLICATE_OPTION_NAME);
        }
    }

    // 주문 가능 여부
    public boolean isOrderable() {
        return status == MenuStatus.SALE && getDeletedAt() == null;
    }

    // 상태 변경
    public void changeStatus(MenuStatus status) {
        this.status = status;
    }

    // 상품 노출 여부
    public boolean isVisible() {
        return status != MenuStatus.HIDDEN && getDeletedAt() == null;
    }

}
