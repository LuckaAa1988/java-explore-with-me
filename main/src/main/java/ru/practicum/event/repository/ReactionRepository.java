package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.entity.UserEventReaction;
import ru.practicum.event.entity.UserEventReactionId;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<UserEventReaction, UserEventReactionId> {
    @Query(value = "SELECT r.event_id, SUM(r.reaction) FROM user_event_reactions AS r " +
            "GROUP BY r.event_id ORDER BY SUM(r.reaction) DESC LIMIT :size OFFSET :from", nativeQuery = true)
    List<Tuple> findAllTop(Integer from, Integer size);

    @Query(value = "SELECT u.id, u.name, SUM(r.reaction) FROM user_event_reactions AS r " +
            "LEFT JOIN public.events AS e on r.event_id = e.id RIGHT JOIN users AS u on e.initiator_id = u.id GROUP BY u.id, u.name " +
            "ORDER BY SUM(r.reaction) LIMIT :size OFFSET :from", nativeQuery = true)
    List<Tuple> findAllTopUsers(Integer from, Integer size);
}
