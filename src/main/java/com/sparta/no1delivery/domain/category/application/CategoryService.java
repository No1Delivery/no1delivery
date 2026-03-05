package com.sparta.no1delivery.domain.category.application;

import com.sparta.no1delivery.domain.category.application.dto.CategoryServiceDto;
import com.sparta.no1delivery.domain.category.domain.Category;
import com.sparta.no1delivery.domain.category.domain.CategoryId;
import com.sparta.no1delivery.domain.category.domain.CategoryRepository;
import com.sparta.no1delivery.global.domain.RoleCheck;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final RoleCheck roleCheck;
    private final CategoryRepository repository;

    //카테고리 생성
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(UUID id, String name, long displayOrder) {
        Category category = Category.builder()
                .categoryId(id)
                .name(name)
                .displayOrder(displayOrder)
                .roleCheck(roleCheck)
                .build();

        repository.save(category);
    }

    // 카테고리 전체 조회
    public List<Category> getCategories() {
        return repository.findAll();
    }

    // 카테고리 조회
    private List<Category> getCategories(List<UUID> categoryIds) {
        List<CategoryId> ids = categoryIds.stream().map(CategoryId::of).toList();
        List<Category> items = repository.findAllById(ids);

        // 요청한 개수와 DB에서 찾은 개수가 다르면 등록되지 않은 ID가 포함된 것임
        if (items.size() != categoryIds.size()) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        return items;
    }

    // 카테고리 수정 (이름 및 순서 일괄 변경)
    @Transactional
    public void change(List<CategoryServiceDto.Category> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new CustomException(ErrorCode.MISSING_INPUT_VALUE);
        }

        Map<UUID, CategoryServiceDto.Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(CategoryServiceDto.Category::getId, c -> c));

        List<Category> items = getCategories(new ArrayList<>(categoryMap.keySet()));

        if (items.size() != categories.size()) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        items.forEach(item -> {
            CategoryServiceDto.Category dto = categoryMap.get(item.getId().getId());
            if (dto != null) {
                item.change(dto.getName(), dto.getDisplayOrder(), roleCheck);
            }
        });
    }


    // 카테고리 삭제
    @Transactional
    public void delete(UUID id) {
        Category category = repository.findById(CategoryId.of(id))
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        repository.delete(category);
    }



}
