package com.sparta.no1delivery.domain.store.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "메뉴 관련 요청 DTO")
public class MenuRequestDto {

    @Data
    @Schema(description = "메뉴 등록 요청 정보")
    public static class Save {
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @Schema(description = "메뉴 이름", example = "후라이드 치킨")
        private String name;

        @Schema(description = "메뉴 상세 설명", example = "겉바속촉의 정석, 천연 재료로 염지한 치킨")
        private String description;

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        @Schema(description = "메뉴 가격", example = "18000", minimum = "0")
        private int price;

        @Valid
        @Schema(description = "메뉴 옵션 리스트")
        private List<Option> options;
    }

    @Data
    @Schema(description = "메뉴 옵션 리스트")
    public static class Option {

        @NotBlank(message = "옵션 이름은 필수입니다.")
        @Schema(description = "옵션명", example = "토핑")
        private String name;

        @NotNull(message = "옵션 필수 여부는 필수입니다.")
        @Schema(description = "필수 선택 여부", example = "true")
        private boolean isEssential;

        @NotNull(message = "옵션 중복 여부는 필수입니다.")
        @Schema(description = "다중 선택 가능 여부", example = "false")
        private boolean isMultiple;

        @Valid
        @NotEmpty(message = "옵션 항목은 필수입니다.")
        @Schema(description = "세부 옵션 항목 리스트")
        private List<SubOption> subOptions;
    }

    @Data
    @Schema(description = "세부 옵션 항목 정보")
    public static class SubOption {

        @NotBlank(message = "옵션 항목 이름은 필수입니다.")
        @Schema(description = "세부 옵션명", example = "치즈 추가")
        private String name;

        @NotNull(message = "옵션 항목 가격은 필수입니다.")
        @Schema(description = "세부 옵션 추가 금액 (없으면 0)", example = "1000")
        private int addPrice;
    }
}