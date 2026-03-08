package com.sparta.no1delivery.domain.store.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRequestDto {

    @Data
    public static class Save {
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        private String name;

        private String description;

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        private int price;

        @Valid
        private List<Option> options;
    }

    @Data
    public static class Option {

        @NotBlank(message = "옵션 이름은 필수입니다.")
        private String name;

        @NotNull(message = "옵션 필수 여부는 필수입니다.")
        private boolean isEssential;

        @NotNull(message = "옵션 중복 여부는 필수입니다.")
        private boolean isMultiple;

        @Valid
        @NotEmpty(message = "옵션 항목은 필수입니다.")
        private List<SubOption> subOptions;
    }

    @Data
    public static class SubOption {

        @NotBlank(message = "옵션 항목 이름은 필수입니다.")
        private String name;

        @NotNull(message = "옵션 항목 가격은 필수입니다.")
        private int addPrice;
    }
}
