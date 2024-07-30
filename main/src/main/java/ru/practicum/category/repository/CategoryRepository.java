package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.category.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    @Query("DELETE FROM Category WHERE id = :categoryId")
    int deleteCategoryById(Long categoryId);

    boolean existsByName(String name);

    Category findByName(String name);
}
