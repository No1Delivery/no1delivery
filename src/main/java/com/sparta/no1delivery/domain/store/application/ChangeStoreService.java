package com.sparta.no1delivery.domain.store.application;

import com.sparta.no1delivery.domain.store.application.dto.StoreServiceDto;
import com.sparta.no1delivery.domain.store.domain.Store;
import com.sparta.no1delivery.domain.store.domain.StoreId;
import com.sparta.no1delivery.domain.store.domain.StoreRepository;
import com.sparta.no1delivery.domain.store.domain.StoreStatus;
import com.sparta.no1delivery.domain.store.domain.dto.StoreDto;
import com.sparta.no1delivery.domain.store.domain.service.CategoryCheck;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeStoreService {

    private final StoreRepository storeRepository;
    private final RoleCheck roleCheck;
    private final OwnerCheck ownerCheck;
    private final CategoryCheck categoryCheck;
    private final AddressToCoords addressToCoords;

    // 가게 정보 수정
    @Transactional
    public void updateStoreInfo(UUID storeId, StoreServiceDto.StoreInfo dto) {
        Store store = getStore(storeId);
        store.updateInfo(StoreDto.StoreInfoDto.builder()
                        .roleCheck(roleCheck)
                        .ownerCheck(ownerCheck)
                        .ownerName(dto.getOwnerName())
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .phone(dto.getPhone())
                        .address(dto.getAddress())
                        .detailAddress(dto.getDetailAddress())
                        .addressToCoords(addressToCoords)
                .build());
    }

    // 가게 상태 변경
    @Transactional
    public void changeStoreStatus(UUID storeId, String status) {
        Store store = getStore(storeId);

        StoreStatus newStatus = StoreStatus.from(status);
        store.changeStatus(roleCheck, ownerCheck, newStatus);
    }

    // 카테고리 추가
    @Transactional
    public void addCategory(UUID storeId, List<UUID> categoryIds) {
        Store store = getStore(storeId);
        store.createCategory(toCategoryDto(categoryIds));
    }

    // 카테고리 교체
    @Transactional
    public void replaceCategory(UUID storeId, List<UUID> categoryIds) {
        Store store = getStore(storeId);
        store.replaceCategory(toCategoryDto(categoryIds));
    }

    // 카테고리 비우기
    @Transactional
    public void truncateCategory(UUID storeId) {
        Store store = getStore(storeId);
        store.truncateCategory(roleCheck, ownerCheck);
    }

    // 카테고리 제거
    @Transactional
    public void removeCategory(UUID storeId, List<UUID> categoryIds) {
        Store store = getStore(storeId);
        store.removeCategory(toCategoryDto(categoryIds));
    }

    // 가게 조회
    private Store getStore(UUID storeId) {
        return storeRepository.findById(StoreId.of(storeId))
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    // 카테고리 아이디 리스트 DTO 변환
    private StoreDto.CategoryDto toCategoryDto(List<UUID> categoryIds) {
        return StoreDto.CategoryDto.builder()
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .categoryCheck(categoryCheck)
                .categoryIds(categoryIds)
                .build();
    }


}
