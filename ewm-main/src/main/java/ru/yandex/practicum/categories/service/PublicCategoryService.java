package ru.yandex.practicum.categories.service;

import ru.yandex.practicum.categories.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> findCategories(int from, int size);

    CategoryDto findCategoryById(int id);
}
