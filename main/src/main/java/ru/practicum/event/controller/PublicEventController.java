package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventResponse;
import ru.practicum.dto.event.EventShortResponse;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortResponse>> getAllEventsWithFilters(@RequestParam(required = false) String text,
                                                                            @RequestParam(required = false) Integer[] categories,
                                                                            @RequestParam(required = false) Boolean paid,
                                                                            @RequestParam(required = false) String rangeStart,
                                                                            @RequestParam(required = false) String rangeEnd,
                                                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                                            @RequestParam(required = false) String sort,
                                                                            @RequestParam(defaultValue = "0") Integer from,
                                                                            @RequestParam(defaultValue = "10") Integer size,
                                                                            HttpServletRequest request) throws InvalidParametersException {
        return ResponseEntity.ok(eventService.getAllEventsWithFilters(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId,
                                                      HttpServletRequest request) throws NotFoundException {
        return ResponseEntity.ok(eventService.getEventById(eventId, request));
    }
}
