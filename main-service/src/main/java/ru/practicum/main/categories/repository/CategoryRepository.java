package ru.practicum.main.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
