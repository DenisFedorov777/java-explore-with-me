package ru.practicum.main.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.categories.model.dto.CategoryDto;
import ru.practicum.main.categories.repository.CategoryRepository;
import ru.practicum.main.exceptions.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.state.Pagination.patternPageable;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Transactional
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("Создание новой категории: {}", categoryDto.getName());
        return CategoryMapper.toDtoCategory(repository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        category.setName(categoryDto.getName());
        return CategoryMapper.toDtoCategory(repository.save(category));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pagination = patternPageable(from, size);
        return repository.findAll(pagination).stream()
                .map(CategoryMapper::toDtoCategory)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.toDtoCategory(repository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена")));
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        log.info("Удаление категории с идентификатором: {}", catId);
        repository.delete(repository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена и не удалена")));
    }
}
