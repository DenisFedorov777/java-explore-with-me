package ru.practicum.main;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsRequestDto;
import ru.practicum.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsClient {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ClientService clientService;

    /*public StatsClient(@Value("http://localhost:9090") String url) {
        this.webClient = WebClient.create(url);
    }*/

    public void saveStat(HttpServletRequest request) {
        EndpointHitDto hit = toHit(request);
        clientService
                .client()
                .post()
                .uri("/hit")
                .body(Mono.just(hit), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(StatsRequestDto requestDto) {
        return clientService
                .client()
                .get()
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

    public Long getViews(LocalDateTime start, LocalDateTime end, List<Long> eventsIds) {
        Long currentViews = 0L;
        if (eventsIds.isEmpty()) {
            return currentViews;
        }
        List<String> uris = new ArrayList<>();
        for (Long eventId : eventsIds) {
            uris.add("/events/" + eventId);
        }
        StatsRequestDto requestDto = new StatsRequestDto(start, end, uris, true);
        List<ViewStatsDto> stats = getStats(requestDto).getBody();
        if (stats != null && !stats.isEmpty()) {
            for (ViewStatsDto stat : stats) {
                currentViews += stat.getHits();
            }
        }
        return currentViews;
    }

    public EndpointHitDto toHit(HttpServletRequest request) {
        return EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("main-service")
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(TIME_FORMATTER)))
                .build();
    }
}
