package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.entity.Hit;
import ru.practicum.request.HitRequest;
import ru.practicum.response.HitResponse;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
public class StatsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatsService statsService;

    @Autowired
    private ObjectMapper objectMapper;

    private HitRequest hitRequest;
    private HitResponse hitResponse;
    private Hit hit;

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
        hitResponse.setUri("192.163.0.1");
        hitResponse.setHits(1L);
    }

    @Test
    void saveRequestTest() throws Exception {
        when(statsService.saveRequest(any(HitRequest.class))).thenReturn(hit);

        mvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hitRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(hit.getId()))
                .andExpect(jsonPath("$.app").value(hit.getApp()))
                .andExpect(jsonPath("$.uri").value(hit.getUri()))
                .andExpect(jsonPath("$.ip").value(hit.getIp()));
    }

    @Test
    void getAllStatsTest() throws Exception {
        List<HitResponse> hitResponseList = Collections.singletonList(hitResponse);
        when(statsService.getStats(any(String.class), any(String.class), any(String[].class), any(Boolean.class)))
                .thenReturn(hitResponseList);

        mvc.perform(get("/stats")
                        .param("start", "2022-09-06 11:00:23")
                        .param("end", "2035-09-06 11:00:23")
                        .param("uris", "/events/1")
                        .param("unique", "false"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].app").value(hitResponseList.get(0).getApp()))
                .andExpect(jsonPath("$[0].uri").value(hitResponseList.get(0).getUri()))
                .andExpect(jsonPath("$[0].hits").value(hitResponseList.get(0).getHits()));
    }

    @Test
    void getAllStatsNoStartTest() throws Exception {
        List<HitResponse> hitResponseList = Collections.singletonList(hitResponse);
        when(statsService.getStats(any(String.class), any(String.class), any(String[].class), any(Boolean.class)))
                .thenReturn(hitResponseList);

        mvc.perform(get("/stats")
                        .param("end", "2035-09-06 11:00:23")
                        .param("uris", "/events/1")
                        .param("unique", "false"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllStatsNoEndTest() throws Exception {
        List<HitResponse> hitResponseList = Collections.singletonList(hitResponse);
        when(statsService.getStats(any(String.class), any(String.class), any(String[].class), any(Boolean.class)))
                .thenReturn(hitResponseList);

        mvc.perform(get("/stats")
                        .param("start", "2035-09-06 11:00:23")
                        .param("uris", "/events/1")
                        .param("unique", "false"))
                .andExpect(status().isBadRequest());
    }

}
