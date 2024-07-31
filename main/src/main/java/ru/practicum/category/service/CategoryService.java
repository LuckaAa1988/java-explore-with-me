package ru.practicum.category.service;

import ru.practicum.dto.category.CategoryRequest;
import ru.practicum.dto.category.CategoryResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAll(Integer from, Integer size) throws InvalidParametersException;

    CategoryResponse findById(Long categoryId) throws NotFoundException;

    CategoryResponse save(CategoryRequest categoryRequest) throws ConflictException;

    void deleteById(Long categoryId) throws NotFoundException, ConflictException;

    CategoryResponse update(CategoryRequest categoryRequest, Long categoryId) throws NotFoundException, ConflictException;
}
