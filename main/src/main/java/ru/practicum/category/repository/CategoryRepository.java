package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.category.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    void deleteById(Long categoryId);

    boolean existsByName(String name);

    Category findByName(String name);
}
