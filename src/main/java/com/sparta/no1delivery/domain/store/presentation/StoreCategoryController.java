package com.sparta.no1delivery.domain.store.presentation;

import com.sparta.no1delivery.domain.store.application.ChangeStoreService;
import com.sparta.no1delivery.domain.store.presentation.dto.CategoryRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores/{storeId}/categories")
public class StoreCategoryController {

    private final ChangeStoreService changeStoreService;

    // 카테고리 추가
    @PostMapping
    public ResponseEntity<Void> addCategory(
            @PathVariable UUID storeId,
            @RequestBody @Valid CategoryRequestDto.Update request
    ) {
        changeStoreService.addCategory(storeId, request.getCategoryIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 카테고리 전체 교체
    @PutMapping
    public ResponseEntity<Void> replaceCategory(
            @PathVariable UUID storeId,
            @RequestBody @Valid CategoryRequestDto.Update request
    ) {
        changeStoreService.replaceCategory(storeId, request.getCategoryIds());
        return ResponseEntity.ok().build();
    }

    // 특정 카테고리들 삭제
    @DeleteMapping
    public ResponseEntity<Void> removeCategory(
            @PathVariable UUID storeId,
            @RequestBody @Valid CategoryRequestDto.Update request
    ) {
        changeStoreService.removeCategory(storeId, request.getCategoryIds());
        return ResponseEntity.ok().build();
    }

    // 카테고리 전체 삭제
    @DeleteMapping("/all")
    public ResponseEntity<Void> truncateCategory(@PathVariable UUID storeId) {
        changeStoreService.truncateCategory(storeId);
        return ResponseEntity.noContent().build();
    }

}
