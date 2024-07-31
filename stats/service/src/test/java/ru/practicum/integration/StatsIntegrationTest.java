package ru.practicum.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.entity.Hit;
import ru.practicum.exception.model.DateException;
import ru.practicum.repository.StatsRepository;
import ru.practicum.response.HitResponse;
import ru.practicum.service.impl.StatsServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StatsIntegrationTest {

    @Autowired
    private StatsServiceImpl statsService;

    @BeforeAll
    static void setUp(@Autowired StatsRepository statsRepository) {
        insertTestData(statsRepository);
    }

    private static void insertTestData(StatsRepository statsRepository) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse("2023-09-06 11:00:23", dateTimeFormatter);
        Hit hit1 = new Hit();
        hit1.setApp("ewm-main-service");
        hit1.setIp("192.163.0.1");
        hit1.setUri("/events/1");
        hit1.setTimestamp(time);

        Hit hit2 = new Hit();
        hit2.setApp("ewm-main-service");
        hit2.setIp("192.163.0.1");
        hit2.setUri("/events/2");
        hit2.setTimestamp(time);

        statsRepository.saveAndFlush(hit1);
        statsRepository.saveAndFlush(hit2);
    }

    @Test
    void getStatsWithUrisNotUniqueTest() throws DateException {
        String start = "2021-09-06 11:00:23";
        String end = "2035-09-06 11:00:23";
        String[] uris = new String[]{"/events/1"};

        List<HitResponse> responses = statsService.getStats(start, end, uris, false);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits())
        );
    }

    @Test
    void getStatsWithNoUrisNotUniqueTest() throws DateException {
        String start = "2021-09-06 11:00:23";
        String end = "2035-09-06 11:00:23";


        List<HitResponse> responses = statsService.getStats(start, end, null, false);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(2, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits()),
                () -> assertEquals("ewm-main-service", responses.get(1).getApp()),
                () -> assertEquals("/events/2", responses.get(1).getUri()),
                () -> assertEquals(1, responses.get(1).getHits())
        );
    }

    @Test
    void getStatsWithUrisUniqueTest() throws DateException {
        String start = "2021-09-06 11:00:23";
        String end = "2035-09-06 11:00:23";
        String[] uris = new String[]{"/events/1"};

        List<HitResponse> responses = statsService.getStats(start, end, uris, true);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(1, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits())
        );
    }

    @Test
    void getStatsWithNoUrisUniqueTest() throws DateException {
        String start = "2021-09-06 11:00:23";
        String end = "2035-09-06 11:00:23";

        List<HitResponse> responses = statsService.getStats(start, end, null, true);

        assertAll(
                () -> assertNotNull(responses),
                () -> assertEquals(2, responses.size()),
                () -> assertEquals("ewm-main-service", responses.get(0).getApp()),
                () -> assertEquals("/events/1", responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(0).getHits())
        );
    }

}
