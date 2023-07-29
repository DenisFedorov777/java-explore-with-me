package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatsClient {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final WebClient client;

    public StatsClient(@Value("http://localhost:9090") String url) {
        this.client = WebClient.create(url);
    }

    public ResponseEntity<EndpointHitDto> saveStat(EndpointHitDto dto) {
        return client.post()
                .uri("/hit")
                .body(Mono.just(dto), EndpointHitDto.class)
                .retrieve()
                .toEntity(EndpointHitDto.class)
                .block();
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(StatsRequestDto requestDto) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("startTime", requestDto.getStart().format(TIME_FORMATTER))
                        .queryParam("endTime", requestDto.getEnd().format(TIME_FORMATTER))
                        .queryParam("uri", requestDto.getUris())
                        .queryParam("unique", requestDto.getUnique())
                        .build())
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .block();
    }
}
