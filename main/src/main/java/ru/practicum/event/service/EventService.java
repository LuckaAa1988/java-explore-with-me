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
    List<EventResponse> findAllPrivate(Long userId, Integer from, Integer size) throws InvalidParametersException;

    EventResponse save(Long userId, EventRequest eventRequest) throws NotFoundException;

    EventResponse findByUserId(Long userId, Long eventId) throws NotFoundException;

    EventResponse updateByUserId(Long userId, Long eventId, EventUpdateRequest eventUpdateRequest) throws NotFoundException, ConflictException;

    List<EventShortResponse> findAllPublic(String text, Integer[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) throws InvalidParametersException;

    EventResponse findById(Long eventId, HttpServletRequest request) throws NotFoundException;

    List<EventResponse> findAll(Integer[] users, String[] states, Integer[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) throws InvalidParametersException;

    EventResponse update(Long eventId, EventUpdateRequest eventAdminRequest) throws NotFoundException, ConflictException;

    List<RequestResponse> findRequest(Long userId, Long eventId) throws NotFoundException;

    EventRequestStatusUpdateResponse updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) throws ConflictException, NotFoundException;
}
