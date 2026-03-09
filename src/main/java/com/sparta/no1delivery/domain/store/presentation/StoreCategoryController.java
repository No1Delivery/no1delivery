package com.sparta.no1delivery.domain.store.presentation;

import com.sparta.no1delivery.domain.store.application.ChangeStoreService;
import com.sparta.no1delivery.domain.store.presentation.dto.CategoryRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Store Category API", description = "가게의 카테고리 할당 및 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores/{storeId}/categories")
public class StoreCategoryController {

    private final ChangeStoreService changeStoreService;

    @Operation(summary = "가게 카테고리 추가", description = "기존 카테고리 목록에 새로운 카테고리들을 추가합니다.")
    @PostMapping
    public ResponseEntity<Void> addCategory(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @RequestBody @Valid CategoryRequestDto.Update request
    ) {
        changeStoreService.addCategory(storeId, request.getCategoryIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "가게 카테고리 전체 교체", description = "기존 카테고리 설정을 모두 지우고 요청된 목록으로 새로 설정합니다.")
    @PutMapping
    public ResponseEntity<Void> replaceCategory(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @RequestBody @Valid CategoryRequestDto.Update request
    ) {
        changeStoreService.replaceCategory(storeId, request.getCategoryIds());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "특정 카테고리 삭제", description = "가게에 할당된 카테고리 중 요청받은 ID 목록만 제거합니다.")
    @DeleteMapping
    public ResponseEntity<Void> removeCategory(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId,
            @RequestBody @Valid CategoryRequestDto.Update request
    ) {
        changeStoreService.removeCategory(storeId, request.getCategoryIds());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "가게 카테고리 전체 삭제", description = "해당 가게에 설정된 모든 카테고리 연결을 해제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "전체 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음")
    })
    @DeleteMapping("/all")
    public ResponseEntity<Void> truncateCategory(
            @Parameter(description = "가게 ID") @PathVariable UUID storeId) {
        changeStoreService.truncateCategory(storeId);
        return ResponseEntity.noContent().build();
    }

}