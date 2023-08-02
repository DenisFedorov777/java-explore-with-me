package ru.practicum.main.categories.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.categories.model.dto.CategoryDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    /*@Bean
        public ModelMapper mapperMapper() {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }*/
    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toDtoCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
