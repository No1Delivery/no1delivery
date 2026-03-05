package com.sparta.no1delivery.domain.category.presentation.dto;

import com.sparta.no1delivery.domain.category.application.dto.CategoryServiceDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    private UUID id;

    @NotBlank(message = "CATEGORY_REQUIRED")
    private String name;


    public CategoryServiceDto.Category toServiceDto() {
        return CategoryServiceDto.Category.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}