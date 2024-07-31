package ru.practicum.compilation.service;

import ru.practicum.dto.compilation.CompilationRequest;
import ru.practicum.dto.compilation.CompilationResponse;
import ru.practicum.dto.compilation.CompilationUpdateRequest;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface CompilationService {
    List<CompilationResponse> findAll(Boolean pinned, Integer from, Integer size) throws InvalidParametersException;

    CompilationResponse findById(Long compId) throws NotFoundException;

    CompilationResponse save(CompilationRequest compilationRequest);

    void deleteById(Long compId) throws NotFoundException;

    CompilationResponse update(Long compId, CompilationUpdateRequest compilationRequest) throws NotFoundException;
}
