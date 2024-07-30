package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.request.HitRequest;
import ru.practicum.response.HitResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        super(
                new RestTemplateBuilder()
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build(),
                serverUrl
        );
    }
    public void save(HitRequest hitRequest) {
        post("/hit", hitRequest);
    }

    public ResponseEntity<Object> getStats(String start, String end, String[] uris, Boolean unique) {
        StringBuilder url = new StringBuilder("/stats?start={start}&end={end}");
        Map<String, Object> parameters = new HashMap<>(Map.of("start", start,
                "end", end,
                "unique", unique));
        if (uris != null) {
            url.append("&uris={uris}");
            parameters.put("uris", uris);
        }
        if (unique) {
            url.append("&unique={unique}");
        }
        return get(url.toString(), parameters);
    }

    public Integer getViews(String uri) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = LocalDateTime.now().minusYears(1L).format(dateTimeFormatter);
        String end = LocalDateTime.now().format(dateTimeFormatter);
        String[] uris = new String[]{uri};
        ObjectMapper objectMapper = new ObjectMapper();
        List<HitResponse> list = objectMapper.convertValue(getStats(start, end, uris, true).getBody(), new TypeReference<>() {
                });
        return list.size();
    }
}
