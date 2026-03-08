package com.sparta.no1delivery.domain.store.presentation;

import com.sparta.no1delivery.domain.store.application.ChangeStoreService;
import com.sparta.no1delivery.domain.store.application.CreateStoreService;
import com.sparta.no1delivery.domain.store.application.RemoveStoreService;
import com.sparta.no1delivery.domain.store.application.dto.StoreServiceDto;
import com.sparta.no1delivery.domain.store.application.query.StoreQueryService;
import com.sparta.no1delivery.domain.store.presentation.dto.StoreQueryRequestDto;
import com.sparta.no1delivery.domain.store.presentation.dto.StoreRequestDto;
import com.sparta.no1delivery.domain.store.presentation.dto.StoreResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
public class StoreController {

    private final StoreQueryService storeQueryService;
    private final CreateStoreService createStoreService;
    private final ChangeStoreService changeStoreService;
    private final RemoveStoreService removeStoreService;

    // 가게 상세 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok()
                .body(storeQueryService.getStore(storeId));
    }

    // 가게 목록 조회
    @GetMapping
    public ResponseEntity<Page<StoreResponseDto>> getStores(
            @Valid StoreQueryRequestDto request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok()
                .body(storeQueryService.searchStores(request.toSearchCondition(), pageable));
    }

    // 가게 생성
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
        return ResponseEntity.ok().build();
    }

    // 가게 정보 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<Void> updateStoreInfo(
            @PathVariable UUID storeId,
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

    // 가게 상태 변경
    @PatchMapping("/{storeId}")
    public ResponseEntity<Void> changeStoreStatus(
            @PathVariable UUID storeId,
            @RequestParam String status
    ) {
        changeStoreService.changeStoreStatus(storeId, status);
        return ResponseEntity.ok().build();
    }

    // 가게 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> removeStore(@PathVariable UUID storeId) {
        removeStoreService.remove(storeId);
        return ResponseEntity.noContent().build();
    }

}
