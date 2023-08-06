package ru.yandex.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.categories.dto.CategoryDto;
import ru.yandex.practicum.categories.dto.NewCategoryDto;
import ru.yandex.practicum.categories.mapper.CategoryMapper;
import ru.yandex.practicum.categories.model.Category;
import ru.yandex.practicum.categories.repository.CategoryRepository;
import ru.yandex.practicum.exceptions.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Запрос на создание новой категории");
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.fromCategoryDto(newCategoryDto)));
    }

    @Override
    public void deleteCategory(int id) {
        log.info("Запрос на удаление категории с id {}", id);
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Категория с этим id не найдена"));
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto updateCategory(int id, CategoryDto updatedCategory) {
        log.info("Запрос на обновление категории с id {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с этим id не найдена"));
        if (updatedCategory.getName() != null && !updatedCategory.getName().isBlank()) {
            category.setName(updatedCategory.getName());
        }
        return CategoryMapper.toCategoryDto(category);
    }
}
