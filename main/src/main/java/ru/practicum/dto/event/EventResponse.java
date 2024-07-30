package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.category.CategoryResponse;
import ru.practicum.dto.user.UserShortResponse;
import ru.practicum.event.util.State;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {
    String annotation;
    CategoryResponse category;
    Integer confirmedRequests;
    LocalDateTime createdOn;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Long id;
    UserShortResponse initiator;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    State state;
    String title;
    Integer views;
}
