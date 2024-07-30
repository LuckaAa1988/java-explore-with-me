package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.category.entity.Category;
import ru.practicum.event.entity.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findEventByInitiatorIdAndId(Long userId, Long eventId);

    List<Event> findAllByCategory(Category category);
}
