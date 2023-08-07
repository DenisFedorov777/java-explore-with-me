package ru.practicum.main;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsService {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsClient webClient;

    public void saveHit(HttpServletRequest request) {
        EndpointHitDto hit = toHit(request);
        webClient
                .webClientWithTimeout()
                .post()
                .uri("/hit")
                .body(Mono.just(hit), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(LocalDateTime start,
             LocalDateTime end,
             List<String> uris,
             Boolean unique) {
        return webClient
                .webClientWithTimeout()
                .get()
                .uri(builder -> builder.path("/stats")
                        .queryParam("start", (start).format(formatter))
                        .queryParam("end", (end).format(formatter))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
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
        List<ViewStatsDto> stats = getStats(start, end, uris, true).getBody();
        if (stats != null && !stats.isEmpty()) {
            for (ViewStatsDto stat : stats) {
                currentViews += stat.getHits();
            }
        }
        return currentViews;
    }

    private EndpointHitDto toHit(HttpServletRequest request) {
        return EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("main-service")
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }
}
