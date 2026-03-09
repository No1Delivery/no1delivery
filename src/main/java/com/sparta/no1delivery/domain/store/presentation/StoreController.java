package com.sparta.no1delivery.domain.store.presentation;

import com.sparta.no1delivery.domain.store.application.ChangeStoreService;
import com.sparta.no1delivery.domain.store.application.CreateStoreService;
import com.sparta.no1delivery.domain.store.application.RemoveStoreService;
import com.sparta.no1delivery.domain.store.application.dto.StoreServiceDto;
import com.sparta.no1delivery.domain.store.application.query.StoreQueryService;
import com.sparta.no1delivery.domain.store.presentation.dto.StoreQueryRequestDto;
import com.sparta.no1delivery.domain.store.presentation.dto.StoreRequestDto;
import com.sparta.no1delivery.domain.store.presentation.dto.StoreResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Store API", description = "가게 등록, 조회, 수정 및 상태 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class StoreController {

    private final StoreQueryService storeQueryService;
    private final CreateStoreService createStoreService;
    private final ChangeStoreService changeStoreService;
    private final RemoveStoreService removeStoreService;

    @Operation(summary = "가게 상세 조회", description = "가게 ID를 이용해 특정 가게의 상세 정보를 조회합니다.")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(
            @Parameter(description = "가게 ID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID storeId) {
        return ResponseEntity.ok()
                .body(storeQueryService.getStore(storeId));
    }

    @Operation(summary = "가게 목록 조회 및 검색", description = "카테고리, 상태, 검색어 및 위치 기반 필터링을 통해 가게 목록을 페이징 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<StoreResponseDto>> getStores(
            @ParameterObject @Valid StoreQueryRequestDto request,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok()
                .body(storeQueryService.searchStores(request.toSearchCondition(), pageable));
    }

    @Operation(summary = "가게 신규 생성", description = "새로운 가게를 등록합니다. 카테고리 ID 목록을 포함할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "가게 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<Void> createStore(@RequestBody @Valid StoreRequestDto.Create request) {
        StoreServiceDto.CreateStore serviceDto = StoreServiceDto.CreateStore.builder()
                .name(request.getName())
                .description(request.getDescription())
                .phone(request.getPhone())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .categoryIds(request.getCategoryIds())
                .build();
        createStoreService.create(serviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "가게 기본 정보 수정", description = "가게의 이름, 설명, 전화번호, 주소 정보를 업데이트합니다.")
    @PutMapping("/{storeId}")
    public ResponseEntity<Void> updateStoreInfo(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @RequestBody @Valid StoreRequestDto.UpdateInfo request
    ) {
        StoreServiceDto.StoreInfo serviceDto = StoreServiceDto.StoreInfo.builder()
                .name(request.getName())
                .description(request.getDescription())
                .phone(request.getPhone())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .build();
        changeStoreService.updateStoreInfo(storeId, serviceDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "가게 영업 상태 변경", description = "가게의 운영 상태(영업중, 휴업 등)를 변경합니다.")
    @PatchMapping("/{storeId}")
    public ResponseEntity<Void> changeStoreStatus(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @Parameter(description = "변경할 상태 코드", example = "OPEN") @RequestParam String status
    ) {
        changeStoreService.changeStoreStatus(storeId, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "가게 삭제", description = "가게 정보를 시스템에서 삭제(Soft Delete)합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음")
    })
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> removeStore(@Parameter(description = "가게 ID") @PathVariable UUID storeId) {
        removeStoreService.remove(storeId);
        return ResponseEntity.noContent().build();
    }
}