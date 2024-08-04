package ru.practicum.category.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryRequest;
import ru.practicum.dto.category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.EventSpecification;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.util.Constants;
import ru.practicum.mapper.category.CategoryMapper;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findAll(Integer from, Integer size) throws InvalidParametersException {
        if (from < 0) throw new InvalidParametersException("Invalid parameters");
        var pageable = PageRequest.of(from / size, size);
        log.info("Get all categories with pageable: {}", pageable);
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findById(Long categoryId) throws NotFoundException {
        log.info("Get category by id {}", categoryId);
        return categoryMapper.toDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(Constants.CATEGORY_NOT_FOUND, categoryId))));
    }

    @Override
    @Transactional
    public CategoryResponse save(CategoryRequest categoryRequest) throws ConflictException {
        log.info("Add category with request {}", categoryRequest);
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ConflictException("Duplicate category name.");
        }
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.fromDto(categoryRequest)));
    }

    @Override
    @Transactional
    public void deleteById(Long categoryId) throws NotFoundException, ConflictException {
        log.info("Remove category with id {}", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException(String.format(Constants.CATEGORY_NOT_FOUND, categoryId));
        }
        if (eventRepository.exists(EventSpecification.byCategories(new Integer[]{categoryId.intValue()}))) {
            throw new ConflictException("This category have events.");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public CategoryResponse update(CategoryRequest categoryRequest, Long categoryId) throws NotFoundException, ConflictException {
        log.info("Update category with id {}", categoryId);
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(Constants.CATEGORY_NOT_FOUND, categoryId)));
        var cat = categoryRepository.findByName(categoryRequest.getName());
        if (cat != null && !cat.getId().equals(categoryId)) {
            throw new ConflictException("Duplicate category name.");
        }
        category.setName(categoryRequest.getName());
        categoryRepository.saveAndFlush(category);
        return categoryMapper.toDto(category);
    }
}
