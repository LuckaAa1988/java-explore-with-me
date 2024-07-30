package ru.practicum.event.service;

import ru.practicum.dto.event.EventShortResponse;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.dto.event.EventRequest;
import ru.practicum.dto.event.EventResponse;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResponse;
import ru.practicum.dto.request.RequestResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventResponse> getAllUserEvents(Long userId, Integer from, Integer size) throws InvalidParametersException;

    EventResponse addEvent(Long userId, EventRequest eventRequest) throws NotFoundException;

    EventResponse getUserEvent(Long userId, Long eventId) throws NotFoundException;

    EventResponse updateUserEvent(Long userId, Long eventId, EventUpdateRequest eventUpdateRequest) throws NotFoundException, ConflictException;

    List<EventShortResponse> getAllEventsWithFilters(String text, Integer[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) throws InvalidParametersException;

    EventResponse getEventById(Long eventId, HttpServletRequest request) throws NotFoundException;

    List<EventResponse> getAllEvents(Integer[] users, String[] states, Integer[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) throws InvalidParametersException;

    EventResponse updateAdminEvent(Long eventId, EventUpdateRequest eventAdminRequest) throws NotFoundException, ConflictException;

    List<RequestResponse> getUserEventRequests(Long userId, Long eventId) throws NotFoundException;

    EventRequestStatusUpdateResponse updateUserEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) throws ConflictException, NotFoundException;
}
