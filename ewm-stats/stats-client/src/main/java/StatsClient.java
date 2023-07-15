import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.RequestDto;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> hit(RequestDto requestDto) {
        return post("/hit", null, null, requestDto);
    }

    public ResponseEntity<Object> stats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        return get("stats?start={start}&end={end}&uris={uris}&unique={unique}", null, Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique));
    }
}
