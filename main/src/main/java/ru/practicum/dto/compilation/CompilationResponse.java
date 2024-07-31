package ru.practicum.dto.compilation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.event.EventResponse;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationResponse {
    Long id;
    Boolean pinned;
    String title;
    List<EventResponse> events;
}
