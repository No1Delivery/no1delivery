package com.sparta.no1delivery.domain.store.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "가게 관련 요청 DTO")
public class StoreRequestDto {

    @Data
    @Schema(description = "가게 등록 요청 정보")
    public static class Create {

        @NotBlank(message = "가게 이름은 필수입니다.")
        @Schema(description = "가게 이름", example = "치킨 가게")
        private String name;

        @NotBlank(message = "가게 설명은 필수입니다.")
        @Schema(description = "가게 상세 설명", example = "30년 전통의 바삭한 치킨 맛집입니다.")
        private String description;

        @NotBlank(message = "가게 전화번호는 필수입니다.")
        @Pattern(
                regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        @Schema(description = "가게 전화번호 (지역번호 포함)", example = "010-1234-5678")
        private String phone;

        @NotBlank(message = "가게 주소는 필수입니다.")
        @Schema(description = "가게 기본 주소", example = "서울특별시 서초구 서초대로 396")
        private String address;

        @Schema(description = "가게 상세 주소", example = "강남빌딩 15층")
        private String detailAddress;

        @Schema(description = "연결할 카테고리 ID 목록", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]")
        private List<UUID> categoryIds;
    }

    @Data
    @Schema(description = "가게 기본 정보 수정 요청")
    public static class UpdateInfo {
        @NotBlank(message = "가게 이름은 필수입니다.")
        @Schema(description = "수정할 가게 이름", example = "피자 가게")
        private String name;

        @NotBlank(message = "가게 설명은 필수입니다.")
        @Schema(description = "수정할 상세 설명", example = "더욱 맛있어진 레시피로 돌아왔습니다.")
        private String description;

        @NotBlank(message = "가게 전화번호는 필수입니다.")
        @Pattern(
                regexp = "^0\\d{1,2}-\\d{3,4}-\\d{4}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        @Schema(description = "수정할 전화번호", example = "010-1234-4578")
        private String phone;

        @NotBlank(message = "가게 주소는 필수입니다.")
        @Schema(description = "수정할 기본 주소", example = "서울특별시 강남구 테헤란로 427")
        private String address;

        @Schema(description = "수정할 상세 주소", example = "아이타워 2층")
        private String detailAddress;
    }
}