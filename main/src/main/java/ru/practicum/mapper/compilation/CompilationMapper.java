package ru.practicum.mapper.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.entity.Compilation;
import ru.practicum.dto.compilation.CompilationRequest;
import ru.practicum.dto.compilation.CompilationResponse;
import ru.practicum.event.entity.Event;
import ru.practicum.mapper.event.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public CompilationResponse toDto(Compilation compilation) {
        return CompilationResponse.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvent().stream()
                        .map(e -> eventMapper.toDto(e, 1))
                        .collect(Collectors.toList()))
                .build();
    }

    public Compilation fromDto(CompilationRequest compilationRequest, List<Event> events) {
        return Compilation.builder()
                .title(compilationRequest.getTitle())
                .pinned(compilationRequest.getPinned())
                .event(events)
                .build();
    }
}
