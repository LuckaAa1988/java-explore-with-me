package ru.practicum.compilation.service;

import ru.practicum.dto.compilation.CompilationRequest;
import ru.practicum.dto.compilation.CompilationResponse;
import ru.practicum.dto.compilation.CompilationUpdateRequest;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;

import java.util.List;

public interface CompilationService {
    List<CompilationResponse> getAllCompilation(Boolean pinned, Integer from, Integer size) throws InvalidParametersException;

    CompilationResponse getCompilation(Long compId) throws NotFoundException;

    CompilationResponse addCompilation(CompilationRequest compilationRequest);

    void removeCompilation(Long compId) throws NotFoundException;

    CompilationResponse updateCompilation(Long compId, CompilationUpdateRequest compilationRequest) throws NotFoundException;
}
