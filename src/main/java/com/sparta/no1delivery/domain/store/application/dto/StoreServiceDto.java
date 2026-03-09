package com.sparta.no1delivery.domain.store.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreServiceDto {

    @Getter
    @Builder
    public static class CreateStore {
        private String ownerName;
        private String name;
        private String description;
        private String phone;
        private String address;
        private String detailAddress;
        private List<UUID> categoryIds;
    }

    @Getter
    @Builder
    public static class StoreInfo {
        private String ownerName;
        private String name;
        private String description;
        private String phone;
        private String address;
        private String detailAddress;
    }

    @Getter
    @Builder
    public static class Menu {
        private UUID menuId;
        private String name;
        private String description;
        private int price;
        private List<MenuOption> options;
    }

    @Getter
    @Builder
    public static class MenuOption {
        private String name;
        private int price;
        private boolean isEssential;
        private boolean isMultiple;
        private List<MenuSubOption> subOptions;
    }

    @Getter
    @Builder
    public static class MenuSubOption {
        private String name;
        private int addPrice;
    }

}
