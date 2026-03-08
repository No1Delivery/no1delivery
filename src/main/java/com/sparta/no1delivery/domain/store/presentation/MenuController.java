package com.sparta.no1delivery.domain.store.presentation;

import com.sparta.no1delivery.domain.store.application.dto.StoreServiceDto;
import com.sparta.no1delivery.domain.store.application.menu.ChangeMenuService;
import com.sparta.no1delivery.domain.store.application.menu.CreateMenuService;
import com.sparta.no1delivery.domain.store.application.menu.RemoveMenuService;
import com.sparta.no1delivery.domain.store.application.query.MenuQueryService;
import com.sparta.no1delivery.domain.store.domain.query.dto.MenuQueryDto;
import com.sparta.no1delivery.domain.store.presentation.dto.MenuQueryRequestDto;
import com.sparta.no1delivery.domain.store.presentation.dto.MenuRequestDto;
import com.sparta.no1delivery.domain.store.presentation.dto.MenuResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vi/stores/{storeId}/menus")
public class MenuController {

    private MenuQueryService menuQueryService;
    private CreateMenuService createMenuService;
    private ChangeMenuService changeMenuService;
    private RemoveMenuService removeMenuService;

    // 메뉴 상세 조회
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenu(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId
    ) {
        return ResponseEntity.ok()
                .body(menuQueryService.getMenu(storeId, menuId));
    }

    // 메뉴 목록 조회
    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenus(
            @PathVariable UUID storeId,
            @Valid MenuQueryRequestDto request
    ) {
        MenuQueryDto.Search searchCondition = MenuQueryDto.Search.builder()
                .keyword(request.keyword()).build();
        return ResponseEntity.ok()
                .body(menuQueryService.getMenus(storeId, searchCondition));
    }

    // 메뉴 생성
    @PostMapping
    public ResponseEntity<Void> createMenu(
            @PathVariable UUID storeId,
            @RequestBody @Valid MenuRequestDto.Save request
    ) {
        createMenuService.createMenu(storeId, toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 메뉴 수정
    @PutMapping("/{menuId}")
    public ResponseEntity<Void> updateMenu(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId,
            @RequestBody @Valid MenuRequestDto.Save request
    ) {
        changeMenuService.updateMenu(storeId, menuId, toServiceDto(request));
        return ResponseEntity.ok().build();
    }

    // 메뉴 상태 변경
    @PatchMapping("/{menuId}/status")
    public ResponseEntity<Void> changeMenuStatus(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId,
            @RequestParam String status
    ) {
        changeMenuService.changeMenuStatus(storeId, menuId, status);
        return ResponseEntity.ok().build();
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> removeMenu(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId
    ) {
        removeMenuService.removeMenu(storeId, menuId);
        return ResponseEntity.noContent().build();
    }

    private StoreServiceDto.Menu toServiceDto(MenuRequestDto.Save request) {
        return StoreServiceDto.Menu.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .options(request.getOptions() == null ? null :
                        request.getOptions().stream().map(this::toOptionServiceDto).toList())
                .build();
    }

    private StoreServiceDto.MenuOption toOptionServiceDto(MenuRequestDto.Option option) {
        return StoreServiceDto.MenuOption.builder()
                .name(option.getName())
                .isEssential(option.isEssential())
                .isMultiple(option.isMultiple())
                .subOptions(option.getSubOptions().stream()
                        .map(so -> StoreServiceDto.MenuSubOption.builder()
                                .name(so.getName())
                                .addPrice(so.getAddPrice())
                                .build())
                        .toList())
                .build();
    }

}
