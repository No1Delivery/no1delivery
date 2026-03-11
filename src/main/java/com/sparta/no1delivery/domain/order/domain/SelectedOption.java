package com.sparta.no1delivery.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectedOption {

    // 옵션 그룹 이름 (예: 사이즈, 토핑)
    private String optionName;

    // 옵션 기본 가격
    private int optionPrice;

    // 선택된 하위 옵션
    private List<SelectedSubOption> subOptions;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectedSubOption {

        // 하위 옵션 이름 (예: L, 치즈추가)
        private String name;

        // 하위 옵션 추가 가격
        private int addPrice;
    }
}