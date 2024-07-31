package ru.practicum.service;

import ru.practicum.entity.Hit;
import ru.practicum.exception.model.DateException;
import ru.practicum.request.HitRequest;
import ru.practicum.response.HitResponse;

import java.util.List;

public interface StatsService {
    Hit saveRequest(HitRequest hitRequest);

    List<HitResponse> getStats(String start, String end, String[] uris, Boolean unique) throws DateException;
}
