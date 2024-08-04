package ru.practicum.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.RequestResponse;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.util.State;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.util.Constants;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.request.entity.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.service.RequestService;
import ru.practicum.request.util.Status;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<RequestResponse> findAll(Long userId) {
        log.info("Get all requests by user id: {}", userId);
        return requestRepository.findByRequesterId(userId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestResponse save(Long userId, Long eventId) throws NotFoundException, ConflictException {
        log.info("Get request by user id: {}, with event: {}", userId, eventId);
        var event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_NOT_FOUND, eventId))
        );
        var requester = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.USER_NOT_FOUND, userId))
        );
        if (requester.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("The event initiator cannot add a request to participate in his event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("You cannot participate in an unpublished event");
        }
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Duplicate request to this event");
        }
        if (Objects.equals(event.getParticipants(), event.getParticipantLimit()) && event.getParticipantLimit() != 0) {
            throw new ConflictException("Reached participation limit");
        }
        var request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(Status.PENDING)
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
            event.setParticipants(event.getParticipants() + 1);
        }
        requestRepository.save(request);
        return requestMapper.toDto(request);
    }

    @Override
    @Transactional
    public RequestResponse cancel(Long userId, Long requestId) throws NotFoundException {
        log.info("Cancel request by user id: {}, with request id {}", userId, requestId);
        var request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.REQUEST_NOT_FOUND, requestId))
        );
        request.setStatus(Status.CANCELED);
        return requestMapper.toDto(request);
    }
}
