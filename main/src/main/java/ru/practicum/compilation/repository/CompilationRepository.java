package ru.practicum.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.entity.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Compilation WHERE id = :compId")
    int deleteCompilationById(Long compId);
}
