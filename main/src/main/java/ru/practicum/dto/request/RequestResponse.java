package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.request.util.Status;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestResponse {
    Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    Long event;
    Long requester;
    Status status;
}
