package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestResponse;
import ru.practicum.exception.model.ConflictException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestResponse>> getAllRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getAllRequests(userId));
    }

    @PostMapping
    public ResponseEntity<RequestResponse> addRequest(@PathVariable Long userId,
                                                      @RequestParam Long eventId) throws NotFoundException, ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.addRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestResponse> cancelRequest(@PathVariable Long userId,
                                                         @PathVariable Long requestId) throws NotFoundException {
        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }
}
