package ru.practicum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.entity.Hit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StatsRepositoryTest {

    @Autowired
    private StatsRepository statsRepository;

    @PersistenceContext
    private EntityManager em;

    private Hit hit1;
    private Hit hit2;
    private Hit hit3;
    private LocalDateTime time;

    @BeforeEach
    void setUp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        time = LocalDateTime.parse("2023-09-06 11:00:23", dateTimeFormatter);
        hit1 = Hit.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .timestamp(time)
                .build();
        hit2 = Hit.builder()
                .app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .timestamp(time)
                .build();
        hit3 = Hit.builder()
                .app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .timestamp(time)
                .build();
    }

    @Test
    void findALlWithUrisNoUniqueTest() {
        statsRepository.save(hit1);
        statsRepository.save(hit2);
        statsRepository.save(hit3);
        em.flush();

        var responses = statsRepository.findALlWithUris(time.minusDays(1L),
                time.plusDays(1L), new String[]{hit1.getUri(), hit2.getUri()});

        assertAll(
                () -> assertEquals(2, responses.size()),
                () -> assertEquals(2, responses.get(0).getHits()),
                () -> assertEquals(hit2.getApp(), responses.get(0).getApp()),
                () -> assertEquals(hit2.getUri(), responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(1).getHits()),
                () -> assertEquals(hit1.getApp(), responses.get(1).getApp()),
                () -> assertEquals(hit1.getUri(), responses.get(1).getUri())
        );
    }

    @Test
    void findALlWithUrisUniqueTest() {
        statsRepository.save(hit1);
        statsRepository.save(hit2);
        statsRepository.save(hit3);
        em.flush();

        var responses = statsRepository.findALlWithUrisUnique(time.minusDays(1L),
                time.plusDays(1L), new String[]{hit1.getUri(), hit2.getUri()});

        assertAll(
                () -> assertEquals(2, responses.size()),
                () -> assertEquals(1, responses.get(0).getHits()),
                () -> assertEquals(hit2.getApp(), responses.get(0).getApp()),
                () -> assertEquals(hit2.getUri(), responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(1).getHits()),
                () -> assertEquals(hit1.getApp(), responses.get(1).getApp()),
                () -> assertEquals(hit1.getUri(), responses.get(1).getUri())
        );
    }

    @Test
    void findALlWithNoUrisNoUniqueTest() {
        statsRepository.save(hit1);
        statsRepository.save(hit2);
        statsRepository.save(hit3);
        em.flush();

        var responses = statsRepository.findAllWithOutUris(time.minusDays(1L), time.plusDays(1L));

        assertAll(
                () -> assertEquals(2, responses.size()),
                () -> assertEquals(2, responses.get(0).getHits()),
                () -> assertEquals(hit2.getApp(), responses.get(0).getApp()),
                () -> assertEquals(hit2.getUri(), responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(1).getHits()),
                () -> assertEquals(hit1.getApp(), responses.get(1).getApp()),
                () -> assertEquals(hit1.getUri(), responses.get(1).getUri())
        );
    }

    @Test
    void findALlWithNoUrisUniqueTest() {
        statsRepository.save(hit1);
        statsRepository.save(hit2);
        statsRepository.save(hit3);
        em.flush();

        var responses = statsRepository.findAllWithOutUrisUnique(time.minusDays(1L), time.plusDays(1L));

        assertAll(
                () -> assertEquals(2, responses.size()),
                () -> assertEquals(1, responses.get(0).getHits()),
                () -> assertEquals(hit2.getApp(), responses.get(0).getApp()),
                () -> assertEquals(hit2.getUri(), responses.get(0).getUri()),
                () -> assertEquals(1, responses.get(1).getHits()),
                () -> assertEquals(hit1.getApp(), responses.get(1).getApp()),
                () -> assertEquals(hit1.getUri(), responses.get(1).getUri())
        );
    }
}
