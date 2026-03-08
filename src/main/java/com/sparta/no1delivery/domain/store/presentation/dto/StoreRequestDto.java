package com.sparta.no1delivery.domain.store.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreRequestDto {

    @Data
    public static class Create {

        @NotBlank(message = "가게 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "가게 설명은 필수입니다.")
        private String description;

        @NotBlank(message = "가게 전화번호는 필수입니다.")
        @Pattern(
                regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        private String phone;

        @NotBlank(message = "가게 주소는 필수입니다.")
        private String address;

        private String detailAddress;

        private List<UUID> categoryIds;
    }

    @Data
    public static class UpdateInfo {
        @NotBlank(message = "가게 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "가게 설명은 필수입니다.")
        private String description;

        @NotBlank(message = "가게 전화번호는 필수입니다.")
        @Pattern(
                regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        private String phone;

        @NotBlank(message = "가게 주소는 필수입니다.")
        private String address;

        private String detailAddress;
    }

}
