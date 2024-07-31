package ru.practicum.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateResponse {
    List<RequestResponse> confirmedRequests;
    List<RequestResponse> rejectedRequests;
}
