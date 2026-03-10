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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Menu API", description = "가게별 메뉴 관리 및 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores/{storeId}/menus")
public class MenuController {

    private final MenuQueryService menuQueryService;
    private final CreateMenuService createMenuService;
    private final ChangeMenuService changeMenuService;
    private final RemoveMenuService removeMenuService;

    @Operation(summary = "메뉴 상세 조회", description = "특정 가게의 특정 메뉴 상세 정보를 조회합니다.")
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenu(
            @Parameter(description = "가게 ID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID", example = "678e8400-e29b-41d4-a716-446655440001") @PathVariable UUID menuId
    ) {
        return ResponseEntity.ok()
                .body(menuQueryService.getMenu(storeId, menuId));
    }

    @Operation(summary = "메뉴 목록 조회 및 검색", description = "가게의 전체 메뉴를 조회하거나 키워드로 검색합니다.")
    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getMenus(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @ParameterObject @Valid MenuQueryRequestDto request
    ) {
        MenuQueryDto.Search searchCondition = MenuQueryDto.Search.builder()
                .keyword(request.keyword()).build();
        return ResponseEntity.ok()
                .body(menuQueryService.getMenus(storeId, searchCondition));
    }

    @Operation(summary = "메뉴 생성", description = "가게에 새로운 메뉴를 등록합니다. (옵션 및 세부 옵션 포함 가능)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "메뉴 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패")
    })
    @PostMapping
    public ResponseEntity<Void> createMenu(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @RequestBody @Valid MenuRequestDto.Save request
    ) {
        createMenuService.createMenu(storeId, toServiceDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "메뉴 정보 수정", description = "메뉴의 이름, 설명, 가격 및 옵션 정보를 수정합니다.")
    @PutMapping("/{menuId}")
    public ResponseEntity<Void> updateMenu(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @RequestBody @Valid MenuRequestDto.Save request
    ) {
        changeMenuService.updateMenu(storeId, menuId, toServiceDto(request));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 상태 변경", description = "메뉴의 판매 상태(판매중, 품절, 숨김 등)를 변경합니다.")
    @PatchMapping("/{menuId}/status")
    public ResponseEntity<Void> changeMenuStatus(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId,
            @Parameter(description = "변경할 상태 값", example = "AVAILABLE") @RequestParam String status
    ) {
        changeMenuService.changeMenuStatus(storeId, menuId, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "메뉴 삭제", description = "메뉴를 소프트 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 메뉴를 찾을 수 없음")
    })
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> removeMenu(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "메뉴 ID") @PathVariable UUID menuId
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
