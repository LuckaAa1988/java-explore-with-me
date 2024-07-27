package ru.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.entity.Hit;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatsRepository;
import ru.practicum.request.HitRequest;
import ru.practicum.response.HitResponse;
import ru.practicum.service.impl.StatsServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;
    @Mock
    private HitMapper hitMapper;
    @InjectMocks
    private StatsServiceImpl statsService;
    private Hit hit;
    private HitResponse hitResponse;
    private HitRequest hitRequest;

    @BeforeEach
    void setUp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse("2023-09-06 11:00:23", dateTimeFormatter);

        hitRequest = new HitRequest();
        hitRequest.setApp("ewm-main-service");
        hitRequest.setIp("192.163.0.1");
        hitRequest.setUri("/events/1");
        hitRequest.setTimestamp(time);

        hit = new Hit();
        hit.setApp("ewm-main-service");
        hit.setIp("192.163.0.1");
        hit.setUri("/events/1");
        hit.setTimestamp(time);
        hit.setId(1L);

        hitResponse = new HitResponse();
        hitResponse.setApp("ewm-main-service");
        hitResponse.setUri("/events/1");
        hitResponse.setHits(1L);
    }

    @Test
    void saveRequestTest() {
        when(statsRepository.save(any(Hit.class))).thenReturn(hit);
        when(hitMapper.fromDto(any(HitRequest.class))).thenReturn(hit);

        Hit hitAnswer = statsService.saveRequest(hitRequest);

        assertAll(
                () -> assertNotNull(hitAnswer),
                () -> assertEquals("ewm-main-service", hitAnswer.getApp()),
                () -> assertEquals("192.163.0.1", hitAnswer.getIp()),
                () -> assertEquals("/events/1", hitAnswer.getUri())
        );
    }

    @Test
    void getStatsNoUriNoUniqueTest() {
        when(statsRepository.findAllWithOutUris(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(hitResponse));

        List<HitResponse> responses =
                statsService.getStats("2021-09-06 11:00:23", "2035-09-06 11:00:23", null, false);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits())
        );
    }
    @Test
    void getStatsNoUriUniqueTest() {
        when(statsRepository.findAllWithOutUrisUnique(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(hitResponse));

        List<HitResponse> responses =
                statsService.getStats("2021-09-06 11:00:23", "2035-09-06 11:00:23", null, true);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits())
        );
    }
    @Test
    void getStatsWithUriNoUniqueTest() {
        when(statsRepository.findALlWithUris(any(LocalDateTime.class), any(LocalDateTime.class), any(String[].class)))
                .thenReturn(Collections.singletonList(hitResponse));

        List<HitResponse> responses =
                statsService.getStats("2021-09-06 11:00:23",
                        "2035-09-06 11:00:23", new String[]{"/events/1"}, false);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits())
        );
    }
    @Test
    void getStatsWithUriUniqueTest() {
        when(statsRepository.findALlWithUrisUnique(any(LocalDateTime.class), any(LocalDateTime.class), any(String[].class)))
                .thenReturn(Collections.singletonList(hitResponse));

        List<HitResponse> responses =
                statsService.getStats("2021-09-06 11:00:23",
                        "2035-09-06 11:00:23", new String[]{"/events/1"}, true);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits())
        );
    }
}
