package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.entity.Hit;
import ru.practicum.exception.model.DateException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatsRepository;
import ru.practicum.request.HitRequest;
import ru.practicum.response.HitResponse;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @Override
    public Hit saveRequest(HitRequest hitRequest) {
        log.info("Сохранение HIT с ip: {}, app: {}, uri: {}",
                hitRequest.getIp(), hitRequest.getApp(), hitRequest.getUri());
        return statsRepository.save(hitMapper.fromDto(hitRequest));
    }

    @Override
    public List<HitResponse> getStats(String start, String end, String[] uris, Boolean unique) throws DateException {
        log.info("Отображение всей статистики с {} по {} для Uris: {}", start, end, uris);
        var inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var startTime = LocalDateTime.parse(start, inputFormatter);
        var endTime = LocalDateTime.parse(end, inputFormatter);
        if (startTime.isAfter(endTime)) {
            throw new DateException("Start Time is After End Time");
        }
        if (unique) {
            return uris == null ? statsRepository.findAllWithOutUrisUnique(startTime, endTime) :
                    statsRepository.findALlWithUrisUnique(startTime, endTime, uris);
        } else {
            return uris == null ? statsRepository.findAllWithOutUris(startTime, endTime) :
                                  statsRepository.findALlWithUris(startTime, endTime, uris);
        }
    }
}
