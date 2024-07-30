package ru.practicum.mapper.category;

import org.springframework.stereotype.Component;
import ru.practicum.category.entity.Category;
import ru.practicum.dto.category.CategoryRequest;
import ru.practicum.dto.category.CategoryResponse;

@Component
public class CategoryMapper {

    public Category fromDto(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName())
                .build();
    }

    public CategoryResponse toDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
