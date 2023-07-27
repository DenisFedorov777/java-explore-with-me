package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.InvalidTimeException;
import ru.practicum.service.StatsService;
import ru.rpacticum.EndpointHitDto;
import ru.rpacticum.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatController {

    private final StatsService service;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStat(@Valid @RequestBody EndpointHitDto endpointHit) {
        service.save(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStat(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime startTime,
                                      @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime endTime,
                                      @RequestParam(required = false) List<String> uri,
                                      @RequestParam(defaultValue = "false") Boolean unique) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeException("Время начала должно быть раньше времени окончания!");
        }
        return service.findStats(startTime, endTime, uri, unique);
    }
}
