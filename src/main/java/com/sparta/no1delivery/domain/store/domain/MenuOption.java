package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.domain.store.infrastructure.converter.MenuSubOptionConverter;
import com.sparta.no1delivery.global.domain.Price;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuOption {

    @Column(name = "option_name")
    private String name;

    @Column(name = "sub_options", columnDefinition = "json")
    @Convert(converter = MenuSubOptionConverter.class)
    private List<MenuSubOption> subOptions;
    // 서브옵션 이름 + 가격 JSON (ex: [{"name":"치즈 추가","price":1000}])

    @AttributeOverrides(
            @AttributeOverride(name = "value", column = @Column(name = "price"))
    )
    private Price price;

    @Column(name = "is_essential", nullable = false)
    private boolean isEssential; // 필수 선택 여부

    @Column(name = "is_multiple", nullable = false)
    private boolean isMultiple; // 다중 선택 가능 여부

    @Builder
    protected MenuOption(String name, int price, List<MenuSubOption> subOptions, boolean isEssential, boolean isMultiple)  {
        validateDuplicateSuboptions(subOptions);

        this.name = name;
        this.price = new Price(price);
        this.subOptions = subOptions;
        this.isEssential = isEssential;
        this.isMultiple = isMultiple;
    }

    private void validateDuplicateSuboptions(List<MenuSubOption> subOptions) {
        if (subOptions == null || subOptions.isEmpty()) return;

        long uniqueCount = subOptions.stream()
                .map(MenuSubOption::name)
                .distinct()
                .count();

        if (uniqueCount != subOptions.size()) {
            throw new CustomException(ErrorCode.DUPLICATE_SUB_OPTION_NAME);
        }
    }

}
