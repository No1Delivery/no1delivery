package com.sparta.no1delivery.domain.store.domain.dto;

import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuOption;
import com.sparta.no1delivery.domain.store.domain.MenuSubOption;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.service.CategoryCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class StoreDto {

    @Getter
    @Builder
    public static class StoreInfoDto {
        private RoleCheck roleCheck;
        private OwnerCheck ownerCheck;
        private String ownerName;
        private String name;
        private String description;
        private String phone;
        private String address;
        private String detailAddress;
        private AddressToCoords addressToCoords;
    }

    @Getter
    @Builder
    public static class MenuDto {
        private RoleCheck roleCheck;
        private OwnerCheck ownerCheck;
        private String menuCode;
        private String name;
        private int price;
        private List<MenuOptionDto> options;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MenuOptionDto {
        private String name;
        private int price;
        private List<MenuSubOptionDto> subOptions;
        private boolean isMultiple;
        private boolean isEssential;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MenuSubOptionDto {
        private String name;
        private int addPrice;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CategoryDto {
        private RoleCheck roleCheck;
        private OwnerCheck ownerCheck;
        private CategoryCheck categoryCheck;
        private List<UUID> categoryIds;
    }

    // MenuDto -> Menu
    public static Menu toMenu(StoreId storeId, int productIdx, MenuDto dto) {
        List<MenuOptionDto> optionDtos = dto.getOptions();
        List<MenuOption> options = optionDtos == null ? null : optionDtos.stream().map(StoreDto::toMenuOption).toList();
        return Menu.builder()
                .storeId(storeId)
                .menuIdx(productIdx)
                .menuCode(dto.menuCode)
                .name(dto.getName())
                .price(dto.getPrice())
                .options(options)
                .build();
    }

    // MenuOptionDto -> MenuOption
    public  static MenuOption toMenuOption(MenuOptionDto dto) {
        List<MenuSubOptionDto> subOptionDtos = dto.getSubOptions();

        List<MenuSubOption> subOptions = subOptionDtos == null ? null
                : subOptionDtos.stream().map(s -> new MenuSubOption(s.getName(), s.getAddPrice())).toList();

        return MenuOption
                .builder()
                .name(dto.getName())
                .price(dto.price)
                .subOptions(subOptions)
                .isMultiple(dto.isMultiple)
                .isEssential(dto.isEssential)
                .build();
    }
}