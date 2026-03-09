package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.domain.Price;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
@Entity
@Table(name = "P_MENU")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseUserEntity {

    @EmbeddedId
    private MenuId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "store_id", nullable = false))
    private StoreId storeId;

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
    @CollectionTable(name = "P_MENU_OPTION", joinColumns = @JoinColumn(name = "menu_id"))
    @OrderColumn(name = "option_idx")
    private List<MenuOption> options;

    @Builder
    protected Menu(UUID menuId, StoreId storeId, String name, String description, int price, List<MenuOption> options) {
        this.id = menuId == null ? MenuId.of() : MenuId.of(menuId);
        this.storeId = storeId;
        this.name = name;
        this.description = description;
        this.price = new Price(price);
        this.status = MenuStatus.SALE; // 기본값 판매중

        this.options = new ArrayList<>();
        if (options != null) {
            createOptions(options);
        }
    }

    // 메뉴 수정
    public void update(String name, String description, int price) {
        // 도메인 제약 조건 검사 (예: 가격은 0원 이상이어야 함)
        if (price < 0) throw new CustomException(ErrorCode.INVALID_MENU_PRICE);

        this.name = name;
        this.description = description;
        this.price = new Price(price);
    }

    // 메뉴 삭제
    public void remove(UserDetails userDetails) {
        delete(userDetails);
    }

    // 옵션 여러개 등록
    public void createOptions(List<MenuOption> newOptions) {
        if (newOptions == null || newOptions.isEmpty()) return;
        this.options = Objects.requireNonNullElseGet(this.options, ArrayList::new); // 리스트 없으면 생성

        // 기존 이름들과 입력된 새 이름들을 한 번에 비교하기 위한 Set
        Set<String> currentNames = options.stream()
                .map(MenuOption::getName)
                .collect(Collectors.toCollection(HashSet::new));

        // 입력 데이터 내부 중복 + 기존 데이터와의 중복을 동시 체크
        for (MenuOption newOption : newOptions) {
            if (!currentNames.add(newOption.getName())) { // Set에 이미 있으면 false 반환
                throw new CustomException(ErrorCode.DUPLICATE_OPTION_NAME);
            }
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
