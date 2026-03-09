package com.sparta.no1delivery.domain.store.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuOption;
import com.sparta.no1delivery.domain.store.domain.MenuStatus;
import com.sparta.no1delivery.domain.store.domain.MenuSubOption;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record MenuResponseDto(
    UUID menuId,
    String name,
    String description,
    int price,
    MenuStatus status,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<OptionResponseDto> options
) {
    public static MenuResponseDto fromList(Menu menu) {
        return MenuResponseDto.builder()
                .menuId(menu.getId().getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice().getValue())
                .status(menu.getStatus())
                .build();
    }

    public static MenuResponseDto fromDetail(Menu menu) {
        return MenuResponseDto.builder()
                .menuId(menu.getId().getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice().getValue())
                .status(menu.getStatus())
                .options(menu.getOptions().stream()
                        .map(OptionResponseDto::from)
                        .toList())
                .build();
    }

    @Builder
    public record OptionResponseDto(
        String name,
        int price,
        boolean isEssential,
        boolean isMultiple,
        List<SubOptionResponseDto> subOptions
    ) {
        public static OptionResponseDto from(MenuOption option) {
            return OptionResponseDto.builder()
                    .name(option.getName())
                    .price(option.getPrice().getValue())
                    .isEssential(option.isEssential())
                    .isMultiple(option.isMultiple())
                    .subOptions(option.getSubOptions().stream()
                            .map(SubOptionResponseDto::from)
                            .toList())
                    .build();
        }
    }

    public record SubOptionResponseDto(String name, int addPrice) {
        public static SubOptionResponseDto from(MenuSubOption subOption) {
            return new SubOptionResponseDto(subOption.name(), subOption.addPrice());
        }
    }

}
