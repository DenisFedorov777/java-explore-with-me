package ru.practicum.main.categories.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.categories.model.dto.CategoryDto;
import ru.practicum.main.categories.model.dto.NewCategoryDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static CategoryDto toDtoCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategoryFromNewDto(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }
}
