package com.sparta.no1delivery.domain.store.domain;

import com.sparta.no1delivery.domain.store.domain.dto.StoreDto;
import com.sparta.no1delivery.domain.store.domain.service.CategoryCheck;
import com.sparta.no1delivery.global.domain.BaseUserEntity;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import com.sparta.no1delivery.domain.store.domain.service.OwnerCheck;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@ToString
@Getter
@Table(name = "P_STORE")
@Access(AccessType.FIELD)
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseUserEntity {

    @EmbeddedId
    private StoreId id;

    @Embedded
    private Owner owner;

    @Column(length = 65, name = "store_name")
    private String name;

    @Column(length = 100, name = "description")
    private String description;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Column(name = "phone")
    private String phone;

    @Embedded
    private StoreAddress address;

    @Embedded
    private Rating rating = new Rating(); // 초기값 지정

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "P_STORE_CATEGORY",
            joinColumns = @JoinColumn(name = "store_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "unique_store_category",
                    columnNames = {"store_id", "category_id"}
            )
    )
    @OrderColumn(name = "category_idx")
    private List<StoreCategory> categories;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "store_id")
    private List<Menu> menus;

    @Builder
    public Store(UUID storeId, String ownerName, String name, String description, String phone, String address,String detailAddress, List<UUID> categoryIds, AddressToCoords addressToCoords, RoleCheck roleCheck, OwnerCheck ownerCheck, CategoryCheck categoryCheck) {

        // 등록 권한 체크
        checkAuthority(roleCheck, ownerCheck);

        this.id = storeId == null ? StoreId.of() : StoreId.of(storeId);
        this.owner = new Owner(ownerCheck.getOwnerId(), ownerName);
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.address = new StoreAddress(address, detailAddress, addressToCoords);
        this.status = StoreStatus.CLOSED;

        // 분류 추가
        createCategory(StoreDto.CategoryDto
                .builder()
                .roleCheck(roleCheck)
                .ownerCheck(ownerCheck)
                .categoryCheck(categoryCheck)
                .categoryIds(categoryIds)
                .build());
    }

    // 가게 정보 수정

    // 가게 상태 수정

    // 가게 삭제



    //// 메뉴 관련
    // 메뉴 생성

    // 메뉴 수정

    // 메뉴 삭제 (Soft Delete)

    // 메뉴코드 중복 체크

    // 메뉴코드로 상품 조회


    //// 카테고리 관련
    // 카테고리 생성
    public void createCategory(StoreDto.CategoryDto dto) {
        // 권한 체크
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());
        // 카테고리 유효성 체크
        validateCategoryIds(dto.getCategoryIds(), dto.getCategoryCheck());
        // 카테고리 추가
        addCategories(dto.getCategoryIds());
    }

    // 카테고리 모두 지우기
    public void truncateCategory(RoleCheck roleCheck, OwnerCheck ownerCheck) {
        checkAuthority(roleCheck, ownerCheck);
        if (categories != null) categories.clear();
    }

    // 카테고리 교체
    public void replaceCategory(StoreDto.CategoryDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        validateCategoryIds(dto.getCategoryIds(), dto.getCategoryCheck());

        if (categories != null) categories.clear();
        addCategories(dto.getCategoryIds());
    }

    // 카테고리 제거
    public void removeCategory(StoreDto.CategoryDto dto) {
        checkAuthority(dto.getRoleCheck(), dto.getOwnerCheck());

        List<UUID> targetCategoryIds = dto.getCategoryIds();
        if (targetCategoryIds == null || targetCategoryIds.isEmpty()) return;
        if (this.categories == null || this.categories.isEmpty()) return;

        this.categories.removeIf(category ->
                targetCategoryIds.contains(category.getCategoryId())
        );
    }

    // 카테고리 유효성 검증
    private void validateCategoryIds(List<UUID> categoryIds, CategoryCheck categoryCheck) {
        if (categoryIds == null || categoryIds.isEmpty()) return;

        if (!categoryCheck.exists(categoryIds)) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }
    }

    // 카테고리 내부 생성
    private void addCategories(List<UUID> categoryIds) {
        categories = Objects.requireNonNullElseGet(categories, ArrayList::new);

        Set<UUID> existingCategoryIds = categories.stream()
                .map(StoreCategory::getCategoryId)
                .collect(Collectors.toSet());

        List<StoreCategory> newCategories = categoryIds.stream()
                        .distinct()
                        .filter(id -> !existingCategoryIds.contains(id))
                        .map(StoreCategory::new)
                        .toList();

        categories.addAll(newCategories);
    }



    // 가게 영업 여부

    // 가게 노출 여부


    /**
     * 가게 관련 기능은 가게 주인(OWNER), 관리자(MANAGER, MASTER)만 가능
     * storeId가 null 이라면 신규 등록이므로
     */
    public void checkAuthority(RoleCheck roleCheck, OwnerCheck ownerCheck) {
        // 관리자 권한
        if (roleCheck.hasRole(List.of("MANAGER", "MASTER"))) {
            return;
        }

        // 신규 등록
        if (id == null) {
            if (!roleCheck.hasRole("OWNER")) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            return;
        } else if (!ownerCheck.isOwner(id.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

}
