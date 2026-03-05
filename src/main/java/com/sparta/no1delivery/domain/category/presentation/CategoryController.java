package com.sparta.no1delivery.domain.category.presentation;

import com.sparta.no1delivery.domain.category.application.CategoryService;
import com.sparta.no1delivery.domain.category.application.dto.CategoryServiceDto;
import com.sparta.no1delivery.domain.category.presentation.dto.CategoryRequestDto;
import com.sparta.no1delivery.domain.category.presentation.dto.CategoryResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 전체 조회
    @GetMapping
    public List<CategoryResponseDto.Detail> getAllCategories() {
        return categoryService.getCategories().stream()
                .map(category -> CategoryResponseDto.Detail.builder()
                        .id(category.getId().getId())
                        .name(category.getName())
                        .build())
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategories(
            @RequestBody @Valid List<CategoryRequestDto> request) {

        List<CategoryServiceDto.Category> items = request.stream().map(CategoryRequestDto::toServiceDto).toList();
        List<UUID> createdIds = categoryService.create(items);


        return CategoryResponseDto.of(createdIds);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeCategoryNames(
            @RequestBody @Valid List<CategoryRequestDto> request) {

        categoryService.change(request.stream().map(CategoryRequestDto::toServiceDto).toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategories(@PathVariable("id") UUID id){
        categoryService.delete(id);
    }

}
