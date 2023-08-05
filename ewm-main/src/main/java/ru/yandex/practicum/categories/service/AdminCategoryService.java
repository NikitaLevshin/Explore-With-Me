package ru.yandex.practicum.categories.service;

import ru.yandex.practicum.categories.dto.CategoryDto;
import ru.yandex.practicum.categories.dto.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(int id);

    CategoryDto updateCategory(int id, CategoryDto updatedCategory);
}
