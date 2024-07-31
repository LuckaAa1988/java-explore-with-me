package ru.practicum.mapper.request;

import org.springframework.stereotype.Component;
import ru.practicum.dto.request.RequestResponse;
import ru.practicum.request.entity.Request;

@Component
public class RequestMapper {

    public RequestResponse toDto(Request request) {
        return RequestResponse.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }
}
