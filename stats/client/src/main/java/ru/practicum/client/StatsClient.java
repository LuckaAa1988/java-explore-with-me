package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.request.HitRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class StatsClient extends BaseClient {

    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        super(new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
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
}
