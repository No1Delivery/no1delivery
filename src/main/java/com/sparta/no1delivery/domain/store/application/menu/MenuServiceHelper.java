package com.sparta.no1delivery.domain.store.application.menu;

import com.sparta.no1delivery.domain.store.application.dto.StoreServiceDto;
import com.sparta.no1delivery.domain.store.domain.*;
import com.sparta.no1delivery.domain.store.domain.dto.StoreDto;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuServiceHelper {

    /**
     * StoreServiceDto.Menu -> StoreDto.MenuDto 변환
      */
    public static StoreDto.MenuDto toMenu(RoleCheck roleCheck, OwnerCheck ownerCheck, StoreServiceDto.Menu dto) {
        return StoreDto.MenuDto.builder()
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .options(toOptions(dto.getOptions()))
                .build();
    }

    private static List<StoreDto.MenuOptionDto> toOptions(List<StoreServiceDto.MenuOption> optionsDto) {
        return Optional.ofNullable(optionsDto)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(MenuServiceHelper::toOptionDto)
                .toList();
    }

    private static StoreDto.MenuOptionDto toOptionDto(StoreServiceDto.MenuOption optionDto) {
        return StoreDto.MenuOptionDto.builder()
                .name(optionDto.getName())
                .price(optionDto.getPrice())
                .subOptions(toSubOptions(optionDto.getSubOptions()))
                .isEssential(optionDto.isEssential())
                .isMultiple(optionDto.isMultiple())
                .build();
    }

    private static List<StoreDto.MenuSubOptionDto> toSubOptions(List<StoreServiceDto.MenuSubOption> subOptionsDto) {
        return Optional.ofNullable(subOptionsDto)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(s -> new StoreDto.MenuSubOptionDto(s.getName(), s.getAddPrice()))
                .toList();
    }


    /**
     * StoreServiceDto -> 엔티티 변환
     */
    public static List<MenuOption> toMenuOptionEntities(List<StoreServiceDto.MenuOption> optionsDto) {
        return Optional.ofNullable(optionsDto)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(MenuServiceHelper::toMenuOptionEntity)
                .toList();
    }

    public static MenuOption toMenuOptionEntity(StoreServiceDto.MenuOption dto) {
        return MenuOption.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .subOptions(toMenuSubOptionEntities(dto.getSubOptions()))
                .isEssential(dto.isEssential())
                .isMultiple(dto.isMultiple())
                .build();
    }

    // StoreServiceDto.MenuSubOption 리스트 -> MenuSubOption 리스트
    public static List<MenuSubOption> toMenuSubOptionEntities(List<StoreServiceDto.MenuSubOption> subOptionsDto) {
        return Optional.ofNullable(subOptionsDto)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(s -> new MenuSubOption(s.getName(), s.getAddPrice()))
                .toList();
    }

    // 가게 조회 getStore
    public static Store getStore(UUID storeId, StoreRepository repository) {
        return repository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    // 메뉴 조회 getMenu
    public static Menu getMenu(Store store, UUID menuId) {
        return store.getMenu(MenuId.of(menuId));

    }
}
