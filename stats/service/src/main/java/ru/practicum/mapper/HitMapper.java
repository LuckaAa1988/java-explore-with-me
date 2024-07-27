package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.entity.Hit;
import ru.practicum.request.HitRequest;

@Component
public class HitMapper {
    public Hit fromDto(HitRequest hitRequest) {
        return Hit.builder()
                .app(hitRequest.getApp())
                .uri(hitRequest.getUri())
                .ip(hitRequest.getIp())
                .timestamp(hitRequest.getTimestamp())
                .build();
    }
}
