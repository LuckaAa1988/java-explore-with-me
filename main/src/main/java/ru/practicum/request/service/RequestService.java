package ru.practicum.request.service;

import ru.practicum.dto.request.RequestResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface RequestService {
    List<RequestResponse> getAllRequests(Long userId);

    RequestResponse addRequest(Long userId, Long eventId) throws NotFoundException, ConflictException;

    RequestResponse cancelRequest(Long userId, Long requestId) throws NotFoundException;
}
