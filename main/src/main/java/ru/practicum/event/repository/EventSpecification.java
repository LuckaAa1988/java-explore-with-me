package ru.practicum.event.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.UserEventReaction;
import ru.practicum.event.util.SortAction;
import ru.practicum.event.util.State;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class EventSpecification {

    public static Specification<Event> byUsers(Integer[] users) {
        if (users == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        var usersList = Arrays.asList(users);
        return (root, query, cb) -> root.get("initiator").in(usersList);
    }

    public static Specification<Event> byStates(String[] states) {
        if (states == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        var statesList = Arrays.stream(states).map(State::valueOf).collect(Collectors.toList());
        return (root, query, cb) -> root.get("state").in(statesList);
    }


    public static Specification<Event> byState(State state) {
        return (root, query, cb) -> cb.equal(root.get("state"), state);
    }

    public static Specification<Event> byAnnotation(String text) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
    }

    public static Specification<Event> byDescription(String text) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%");
    }

    public static Specification<Event> byText(String text) {
        if (text == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return Specification.where(byAnnotation(text)).or(byDescription(text));
    }

    public static Specification<Event> byCategories(Integer[] categories) {
        if (categories == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        var categoriesList = Arrays.asList(categories);
        return (root, query, cb) -> root.get("category").in(categoriesList);
    }

    public static Specification<Event> byPaid(Boolean paid) {
        if (paid == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.equal(root.get("paid"), paid);
    }

    public static Specification<Event> startDate(LocalDateTime start) {
        if (start == null) {
            return (root, query, cb) -> cb.greaterThan(root.get("eventDate"), LocalDateTime.now());
        }
        return (root, query, cb) -> cb.greaterThan(root.get("eventDate"), start);
    }

    public static Specification<Event> endDate(LocalDateTime end) {
        if (end == null) {
            return (root, query, cb) -> cb.greaterThan(root.get("eventDate"), LocalDateTime.now());
        }
        return (root, query, cb) -> cb.lessThan(root.get("eventDate"), end);
    }

    public static Specification<Event> byAvailable(Boolean available) {
        if (!available) {
            return (root, query, cb) -> cb.conjunction();
        } else return (root, query, cb) -> cb.equal(root.get("participants"), root.get("participant_limit"));
    }

    public static Specification<Event> orderBy(SortAction sortAction) {
        if (sortAction == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            if (sortAction == SortAction.EVENT_DATE) {
                query.orderBy(cb.desc(root.get("eventDate")));
            } else if (sortAction == SortAction.LIKES) {
                Join<Event, UserEventReaction> reactionsJoin = root.join("reactions", JoinType.INNER);
                query.groupBy(root.get("id"), reactionsJoin.get("reaction"));
                query.orderBy(cb.desc(cb.sum(reactionsJoin.get("reaction"))));
            }
            return cb.conjunction();
        };
    }
}
