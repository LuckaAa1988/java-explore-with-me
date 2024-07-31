package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.compilation.service.CompilationService;
import ru.practicum.dto.compilation.CompilationResponse;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationResponse>> findAll(@RequestParam(required = false) Boolean pinned,
                                                                       @RequestParam(defaultValue = "0") Integer from,
                                                                       @RequestParam(defaultValue = "10") Integer size) throws InvalidParametersException {
        return ResponseEntity.ok(compilationService.findAll(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationResponse> findById(@PathVariable Long compId) throws NotFoundException {
        return ResponseEntity.ok(compilationService.findById(compId));
    }
}
