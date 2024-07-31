package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventRequest;
import ru.practicum.dto.event.EventResponse;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResponse;
import ru.practicum.dto.request.RequestResponse;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> findAllPrivate(@PathVariable Long userId,
                                                                @RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(defaultValue = "10") Integer size) throws InvalidParametersException {
        return ResponseEntity.ok(eventService.findAllPrivate(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventResponse> save(@PathVariable Long userId,
                                                  @RequestBody @Valid EventRequest eventRequest) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.save(userId, eventRequest));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> findByUserId(@PathVariable Long userId,
                                                      @PathVariable Long eventId) throws NotFoundException {
        return ResponseEntity.ok(eventService.findByUserId(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateByUserId(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody @Valid EventUpdateRequest eventUpdateRequest)
            throws NotFoundException, ConflictException {
        return ResponseEntity.ok(eventService.updateByUserId(userId, eventId, eventUpdateRequest));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestResponse>> findRequest(@PathVariable Long userId,
                                                                      @PathVariable Long eventId) throws NotFoundException {
        return ResponseEntity.ok(eventService.findRequest(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResponse> updateRequest(@PathVariable Long userId,
                                                                                    @PathVariable Long eventId,
                                                                                    @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest)
            throws NotFoundException, ConflictException {
        return ResponseEntity.ok(eventService.updateRequest(userId, eventId, eventRequestStatusUpdateRequest));
    }
}
