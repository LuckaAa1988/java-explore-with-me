package ru.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.event.EventShortResponse;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.dto.event.EventRequest;
import ru.practicum.dto.event.EventResponse;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResponse;
import ru.practicum.dto.request.RequestResponse;
import ru.practicum.event.entity.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.event.util.State;
import ru.practicum.event.util.StateAction;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.util.Constants;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.mapper.event.LocationMapper;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.request.HitRequest;
import ru.practicum.request.entity.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.util.Status;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.repository.EventSpecification.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;
    private final StatsClient statsClient;

    @Override
    public List<EventResponse> getAllUserEvents(Long userId, Integer from, Integer size) throws InvalidParametersException {
        log.info("Get all events by user id: {}", userId);
        if (from < 0) throw new InvalidParametersException("Invalid parameters");
        var pageable = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiatorId(userId, pageable).getContent().stream()
                .map(e -> eventMapper.toDto(e, getViews("/events/" + e.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse addEvent(Long userId, EventRequest eventRequest) throws NotFoundException {
        log.info("Add event by user id: {}, request: {}", userId, eventRequest);
        var initiator = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.USER_NOT_FOUND, userId))
        );
        var category = categoryRepository.findById(eventRequest.getCategoryId()).orElseThrow(
                () -> new NotFoundException(String.format(Constants.CATEGORY_NOT_FOUND, eventRequest.getCategoryId()))
        );
        var location = locationRepository.saveAndFlush(locationMapper.fromDto(eventRequest.getLocation()));
        var event = eventMapper.fromDto(eventRequest, category, initiator, location);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setParticipants(0);
        if (eventRequest.getPaid() == null) {
            event.setPaid(false);
        }
        if (eventRequest.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (eventRequest.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        return eventMapper.toDto(eventRepository.save(event), getViews("/events/" + event.getId()));
    }

    @Override
    public EventResponse getUserEvent(Long userId, Long eventId) throws NotFoundException {
        log.info("Get event by user id: {}, event id: {}", userId, eventId);
        return eventMapper.toDto(eventRepository.findEventByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId))
        ), getViews("/events/" + eventId));
    }

    @Override
    public EventResponse updateUserEvent(Long userId, Long eventId, EventUpdateRequest eventUpdateRequest)
            throws NotFoundException, ConflictException {
        log.info("Update event by user id: {}, event id: {}", userId, eventId);
        var event = eventRepository.findEventByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId))
        );
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        if (eventUpdateRequest.getStateAction() != null) {
            if (eventUpdateRequest.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
            if (eventUpdateRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
        }
        return getEventResponse(eventId, eventUpdateRequest, event);
    }

    @Override
    public List<EventShortResponse> getAllEventsWithFilters(String text,
                                                            Integer[] categories,
                                                            Boolean paid,
                                                            String rangeStart,
                                                            String rangeEnd,
                                                            Boolean onlyAvailable,
                                                            String sortString,
                                                            Integer from,
                                                            Integer size,
                                                            HttpServletRequest request) throws InvalidParametersException {
        log.info("Get all events");
        saveHitRequest(request);
        if (from < 0) throw new InvalidParametersException("Invalid parameters");
        var pageable = PageRequest.of(from / size, size);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
            if (end.isBefore(start)) {
                throw new InvalidParametersException("Invalid parameters");
            }
        }
        return eventRepository.findAll(byText(text)
                        .and(byState(State.PUBLISHED))
                        .and(byCategories(categories))
                        .and(byPaid(paid))
                        .and(startDate(start))
                        .and(endDate(end))
                        .and(byAvailable(onlyAvailable))
                        .and(orderBy(sortString)), pageable).getContent().stream()
                .map(e -> eventMapper.toShortDto(e, getViews("/events/" + e.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse getEventById(Long eventId, HttpServletRequest request) throws NotFoundException {
        log.info("Get event by id: {}", eventId);
        saveHitRequest(request);
        var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId))
        );
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId));
        }
        return eventMapper.toDto(event, getViews("/events/" + eventId));
    }

    @Override
    public List<EventResponse> getAllEvents(Integer[] users,
                                            String[] states,
                                            Integer[] categories,
                                            String rangeStart,
                                            String rangeEnd,
                                            Integer from,
                                            Integer size) throws InvalidParametersException {
        log.info("Get all events by admin");
        if (from < 0) throw new InvalidParametersException("Invalid parameters");
        var pageable = PageRequest.of(from / size, size);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
            if (end.isBefore(start)) {
                throw new InvalidParametersException("Invalid parameters");
            }
        }
        return eventRepository.findAll(byUsers(users)
                        .and(byStates(states))
                        .and(byCategories(categories))
                        .and(startDate(start))
                        .and(endDate(end)), pageable).stream()
                .map(e -> eventMapper.toDto(e, getViews("/events/" + e.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse updateAdminEvent(Long eventId, EventUpdateRequest eventUpdateRequest) throws NotFoundException, ConflictException {
        log.info("Update event by admin with id: {}", eventId);
        var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId))
        );
        if (eventUpdateRequest.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ConflictException("This event already: " + event.getState());
            }
            if (eventUpdateRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (eventUpdateRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }
        return getEventResponse(eventId, eventUpdateRequest, event);
    }

    @Override
    public List<RequestResponse> getUserEventRequests(Long userId, Long eventId) throws NotFoundException {
        log.info("Get event requests by user id: {}, event id: {}", userId, eventId);
        var user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.USER_NOT_FOUND, userId))
        );
        var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId))
        );
        return requestRepository.findAllByEvent(event).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResponse updateUserEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequest) throws ConflictException, NotFoundException {
        log.info("Update event request by user id: {}, event id: {}", userId, eventId);
        var requests = requestRepository.findAllById(eventRequest.getRequestIds());
        if (!requests.stream().allMatch(r -> r.getStatus().equals(Status.PENDING))) {
            throw new ConflictException("The status can be changed on requests only with Status: PENDING.");
        }
        var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId))
        );
        List<RequestResponse> confirmedRequests = new ArrayList<>();
        List<RequestResponse> rejectedRequests = new ArrayList<>();
        Status status = eventRequest.getStatus();
        for (Request request : requests) {
            if (event.getParticipants().equals(event.getParticipantLimit()) && event.getParticipantLimit() != 0) {
                throw new ConflictException("Reached participants limit.");
            }
            request.setStatus(status);
            if (status.equals(Status.CONFIRMED)) {
                event.setParticipants(event.getParticipants() + 1);
                confirmedRequests.add(requestMapper.toDto(request));
            } else rejectedRequests.add(requestMapper.toDto(request));
        }
        requestRepository.saveAllAndFlush(requests);
        return EventRequestStatusUpdateResponse.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    private EventResponse getEventResponse(Long eventId, EventUpdateRequest eventUpdateRequest, Event event) throws NotFoundException {
        if (eventUpdateRequest.getAnnotation() != null) {
            event.setAnnotation(eventUpdateRequest.getAnnotation());
        }
        if (eventUpdateRequest.getCategoryId() != null) {
            var category = categoryRepository.findById(eventUpdateRequest.getCategoryId()).orElseThrow(
                    () -> new NotFoundException(String.format(Constants.CATEGORY_NOT_FOUND, eventUpdateRequest.getCategoryId()))
            );
            event.setCategory(category);
        }
        if (eventUpdateRequest.getDescription() != null) {
            event.setDescription(eventUpdateRequest.getDescription());
        }
        if (eventUpdateRequest.getEventDate() != null) {
            event.setEventDate(eventUpdateRequest.getEventDate());
        }
        if (eventUpdateRequest.getLocation() != null) {
            var location = locationMapper.fromDto(eventUpdateRequest.getLocation());
            locationRepository.saveAndFlush(location);
            event.setLocation(location);
        }
        if (eventUpdateRequest.getPaid() != null) {
            event.setPaid(eventUpdateRequest.getPaid());
        }
        if (eventUpdateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateRequest.getParticipantLimit());
        }
        if (eventUpdateRequest.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateRequest.getRequestModeration());
        }
        if (eventUpdateRequest.getTitle() != null) {
            event.setTitle(eventUpdateRequest.getTitle());
        }
        eventRepository.saveAndFlush(event);
        return eventMapper.toDto(event, getViews("/events/" + eventId));
    }

    private void saveHitRequest(HttpServletRequest request) {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp("ewm-service");
        hitRequest.setUri(request.getRequestURI());
        hitRequest.setIp(request.getRemoteAddr());
        hitRequest.setTimestamp(LocalDateTime.now());
        statsClient.save(hitRequest);
    }

    private Integer getViews(String event) {
        return statsClient.getViews(event);
    }

}
