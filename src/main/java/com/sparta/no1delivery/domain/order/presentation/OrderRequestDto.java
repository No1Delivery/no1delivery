package com.sparta.no1delivery.domain.order.presentation;

import com.sparta.no1delivery.domain.order.application.dto.OrderServiceDto;
import com.sparta.no1delivery.domain.order.domain.query.OrderQueryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OrderRequestDto {

    // 주문자 이름
    private String ordererName;

    // 매장 정보
    @NotNull(message = "매장 ID는 필수입니다.")
    private UUID storeId;
    private String storeName;
    private String storeAddress;
    private String storeTel;

    // 배송 정보
    @NotBlank(message = "배송 주소는 필수입니다.")
    private String deliveryAddress;
    private String deliveryAddressDetail;
    private String deliveryMemo;

    @NotBlank(message = "연락처는 필수입니다.")
    private String phone;

    // 주문 상품 목록 (최소 1개 이상)
    @Valid
    @NotEmpty(message = "최소 1개 이상의 상품을 주문해야 합니다.")
    private List<OrderItemRequest> items;

    // Controller → Service 계층으로 전달할 DTO 변환
    public OrderServiceDto.Create toServiceDto() {

        return OrderServiceDto.Create.builder()
                .ordererName(ordererName)
                .storeId(storeId)
                .storeName(storeName)
                .deliveryAddress(deliveryAddress)
                .deliveryAddressDetail(deliveryAddressDetail)
                .deliveryMemo(deliveryMemo)
                .phone(phone)
                .items(
                        items.stream()
                                .map(item -> OrderServiceDto.Item.builder()
                                        .menuId(item.getMenuId())
                                        .menuName(item.getMenuName())
                                        .quantity(item.getQuantity())
                                        .menuPrice(item.getMenuPrice())
                                        .options(
                                                item.getOptions() == null ? List.of() :
                                                        item.getOptions().stream()
                                                                .map(option -> OrderServiceDto.Option.builder()
                                                                        .name(option.getName())
                                                                        .price(option.getPrice())
                                                                        .subOptions(
                                                                                option.getSubOptions() == null ? List.of() :
                                                                                        option.getSubOptions().stream()
                                                                                                .map(sub -> OrderServiceDto.SubOption.builder()
                                                                                                        .name(sub.getName())
                                                                                                        .price(sub.getPrice())
                                                                                                        .build())
                                                                                                .toList()
                                                                        )
                                                                        .build())
                                                                .toList()
                                        )
                                        .build())
                                .toList()
                )
                .build();
    }

    //주문 상품 정보(메뉴 + 수량 + 옵션 선택)

    @Getter
    public static class OrderItemRequest {

        @NotNull(message = "메뉴 ID는 필수입니다.")
        private UUID menuId;

        private String menuName;

        // 주문 수량 최소 1개
        @Min(value = 1, message = "주문 수량은 최소 1개 이상입니다.")
        private int quantity;

        // 메뉴 기본 가격
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        private int menuPrice;

        // 옵션 그룹 목록
        @Valid
        private List<Option> options;
    }

     //옵션 그룹예: 맵기, 사이즈
    @Getter
    public static class Option {

        @NotBlank(message = "옵션 이름은 필수입니다.")
        private String name;

        // 옵션 추가 가격
        @Min(value = 0, message = "옵션 가격은 0 이상이어야 합니다.")
        private int price;

        // 실제 선택된 옵션들
        @Valid
        private List<SubOption> subOptions;
    }

    //옵션 상세 항목 예: 보통맛, 매운맛
    @Getter
    public static class SubOption {

        @NotBlank(message = "옵션 항목 이름은 필수입니다.")
        private String name;

        @Min(value = 0, message = "옵션 가격은 0 이상이어야 합니다.")
        private int price;
    }

    //주문 검색 필터 DTO (주문 목록 조회 시 조건 검색에 사용)
    @Getter
    public static class Search {

        // 특정 주문 조회
        private List<UUID> orderIds;

        // 주문자 이름 검색
        private String ordererName;

        // 매장 검색
        private List<UUID> storeIds;
        private String storeName;

        // 배송 주소 검색
        private String deliveryAddress;

        // 주문 상태 필터
        private List<String> orderStatuses;

        // Controller DTO → Query DTO 변환
        public OrderQueryDto.Search toQuerySearch() {
            return OrderQueryDto.Search.builder()
                    .orderIds(this.orderIds)
                    .ordererName(this.ordererName)
                    .storeIds(this.storeIds)
                    .storeName(this.storeName)
                    .deliveryAddress(this.deliveryAddress)
                    .orderStatuses(this.orderStatuses)
                    .build();
        }
    }
}