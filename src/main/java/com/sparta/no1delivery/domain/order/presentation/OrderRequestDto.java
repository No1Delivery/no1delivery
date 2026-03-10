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

    // 주문 상품 목록
    @Valid
    @NotEmpty(message = "최소 1개 이상의 상품을 주문해야 합니다.")
    private List<OrderItemRequest> items;


    // Controller → Service DTO 변환
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


    // 주문 상품
    @Getter
    public static class OrderItemRequest {

        @NotNull(message = "메뉴 ID는 필수입니다.")
        private UUID menuId;

        private String menuName;

        @Min(value = 1, message = "주문 수량은 최소 1개 이상입니다.")
        private int quantity;

        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        private int menuPrice;

        @Valid
        private List<Option> options;
    }


    // 옵션 그룹
    @Getter
    public static class Option {

        @NotBlank(message = "옵션 이름은 필수입니다.")
        private String name;

        @Valid
        private List<SubOption> subOptions;
    }


    // 옵션 상세
    @Getter
    public static class SubOption {

        @NotBlank(message = "옵션 항목 이름은 필수입니다.")
        private String name;

        @Min(value = 0, message = "옵션 가격은 0 이상이어야 합니다.")
        private int price;
    }


    // 주문 검색 필터
    @Getter
    public static class Search {

        private List<UUID> orderIds;
        private String ordererName;

        private List<UUID> storeIds;
        private String storeName;

        private String deliveryAddress;

        private List<String> orderStatuses;

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


    // 배송지 변경
    @Getter
    public static class ChangeDelivery {

        @NotBlank(message = "배송 주소는 필수입니다.")
        private String address;

        private String detailAddress;

        private String memo;
    }
}