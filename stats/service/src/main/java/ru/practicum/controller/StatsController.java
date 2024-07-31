package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.model.DateException;
import ru.practicum.request.HitRequest;
import ru.practicum.response.HitResponse;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveRequest(@RequestBody @Valid HitRequest hitRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.saveRequest(hitRequest));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<HitResponse>> getStats(@RequestParam String start,
                                                      @RequestParam String end,
                                                      @RequestParam(required = false) String[] uris,
                                                      @RequestParam(defaultValue = "false") Boolean unique) throws DateException {
        return ResponseEntity.ok().body(statsService.getStats(start, end, uris, unique));
    }
}
