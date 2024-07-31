package ru.practicum.request.service;

import ru.practicum.dto.request.RequestResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface RequestService {
    List<RequestResponse> findAll(Long userId);

    RequestResponse save(Long userId, Long eventId) throws NotFoundException, ConflictException;

    RequestResponse cancel(Long userId, Long requestId) throws NotFoundException;
}
