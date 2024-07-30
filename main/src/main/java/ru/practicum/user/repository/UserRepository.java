package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.user.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByIdIn(List<Long> ids, Pageable pageable);

    @Modifying
    @Query("DELETE FROM User WHERE id = :userId")
    int deleteUserById(Long userId);

    boolean existsByEmail(String email);
}
