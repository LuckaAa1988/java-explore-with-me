package ru.practicum.mapper.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.category.entity.Category;
import ru.practicum.dto.event.EventRequest;
import ru.practicum.dto.event.EventResponse;
import ru.practicum.dto.event.EventShortResponse;
import ru.practicum.event.entity.Event;
import ru.practicum.event.entity.Location;
import ru.practicum.event.entity.UserEventReaction;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.user.entity.User;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final LocationMapper locationMapper;

    public Event fromDto(EventRequest eventRequest, Category category, User initiator, Location location) {
        return Event.builder()
                .description(eventRequest.getDescription())
                .paid(eventRequest.getPaid())
                .annotation(eventRequest.getAnnotation())
                .title(eventRequest.getTitle())
                .eventDate(eventRequest.getEventDate())
                .location(location)
                .participantLimit(eventRequest.getParticipantLimit())
                .requestModeration(eventRequest.getRequestModeration())
                .category(category)
                .initiator(initiator)
                .requestModeration(eventRequest.getRequestModeration())
                .build();
    }

    public EventResponse toDto(Event event, Integer views) {
        int reaction = 0;
        if (event.getReactions() != null) {
            reaction = event.getReactions().stream().mapToInt(UserEventReaction::getReaction).sum();
        }
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .description(event.getDescription())
                .paid(event.getPaid())
                .location(locationMapper.toDto(event.getLocation()))
                .annotation(event.getAnnotation())
                .initiator(userMapper.toShortDto(event.getInitiator()))
                .category(categoryMapper.toDto(event.getCategory()))
                .state(event.getState())
                .participantLimit(event.getParticipantLimit())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(event.getParticipants())
                .views(views)
                .reactions(reaction)
                .build();
    }

    public EventShortResponse toShortDto(Event event, Integer views) {
        int reaction = 0;
        if (event.getReactions() != null) {
            reaction = event.getReactions().stream().mapToInt(UserEventReaction::getReaction).sum();
        }
        return EventShortResponse.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getParticipants())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .reactions(reaction)
                .build();
    }
}
