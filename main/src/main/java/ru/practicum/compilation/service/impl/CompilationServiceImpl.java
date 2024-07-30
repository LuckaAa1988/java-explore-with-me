package ru.practicum.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.dto.compilation.CompilationRequest;
import ru.practicum.dto.compilation.CompilationResponse;
import ru.practicum.dto.compilation.CompilationUpdateRequest;
import ru.practicum.event.entity.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.model.InvalidParametersException;
import ru.practicum.exception.model.NotFoundException;
import ru.practicum.exception.util.Constants;
import ru.practicum.mapper.compilation.CompilationMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationResponse> getAllCompilation(Boolean pinned, Integer from, Integer size) throws InvalidParametersException {
        log.info("Get all compilations with pinned: {}", pinned);
        if (from < 0) throw new InvalidParametersException("Invalid parameters");
        var pageable = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationRepository.findAll(pageable).getContent().stream()
                    .map(compilationMapper::toDto)
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAllByPinned(pinned, pageable).getContent().stream()
                .map(compilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationResponse getCompilation(Long compId) throws NotFoundException {
        log.info("Get compilation with id: {}", compId);
        return compilationMapper.toDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.COMPILATION_NOT_FOUND, compId))
        ));
    }

    @Override
    public CompilationResponse addCompilation(CompilationRequest compilationRequest) {
        log.info("Add compilation with request: {}", compilationRequest);
        List<Event> events = Collections.emptyList();
        if (compilationRequest.getEvents() != null) {
            events = eventRepository.findAllById(compilationRequest.getEvents());
        }
        var compilation = compilationRepository.save(compilationMapper.fromDto(compilationRequest, events));
        return compilationMapper.toDto(compilation);
    }

    @Override
    @Transactional
    public void removeCompilation(Long compId) throws NotFoundException {
        log.info("Delete compilation with id: {}", compId);
        if (compilationRepository.deleteCompilationById(compId) == 0) {
            throw new NotFoundException(String.format(Constants.COMPILATION_NOT_FOUND, compId));
        }
    }

    @Override
    public CompilationResponse updateCompilation(Long compId, CompilationUpdateRequest compilationRequest) throws NotFoundException {
        log.info("Update compilation with id: {}, request: {}", compId, compilationRequest);
        var compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.COMPILATION_NOT_FOUND, compId))
        );
        List<Event> events;
        if (compilationRequest.getEvents() != null) {
            events = eventRepository.findAllById(compilationRequest.getEvents());
            compilation.setEvent(events);
        }
        if (compilationRequest.getTitle() != null) {
            compilation.setTitle(compilationRequest.getTitle());
        }
        if (compilationRequest.getPinned() != null) {
            compilation.setPinned(compilationRequest.getPinned());
        }
        compilationRepository.saveAndFlush(compilation);
        return compilationMapper.toDto(compilation);
    }
}
