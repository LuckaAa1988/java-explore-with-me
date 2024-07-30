package ru.practicum.category.service;

import ru.practicum.dto.category.CategoryRequest;
import ru.practicum.dto.category.CategoryResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories(Integer from, Integer size) throws InvalidParametersException;

    CategoryResponse getCategoryById(Long categoryId) throws NotFoundException;

    CategoryResponse addCategory(CategoryRequest categoryRequest) throws ConflictException;

    void removeCategory(Long categoryId) throws NotFoundException, ConflictException;

    CategoryResponse updateCategory(CategoryRequest categoryRequest, Long categoryId) throws NotFoundException, ConflictException;
}
