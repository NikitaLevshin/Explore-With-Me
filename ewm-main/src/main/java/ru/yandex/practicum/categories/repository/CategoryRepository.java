package ru.yandex.practicum.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.categories.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
