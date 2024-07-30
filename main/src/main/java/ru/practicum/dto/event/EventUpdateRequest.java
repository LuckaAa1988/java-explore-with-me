package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.util.StateAction;
import ru.practicum.validation.FuturePlusHours;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    @JsonProperty("category")
    Long categoryId;
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FuturePlusHours
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    @Min(0)
    Integer participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}
