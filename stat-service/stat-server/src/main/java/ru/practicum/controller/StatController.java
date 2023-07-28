package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class StatController {

    private final StatsService service;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStat(@Valid @RequestBody EndpointHitDto endpointHit) {
        service.save(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStat(@RequestParam(value = "start") @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime startTime,
                                      @RequestParam(value = "end") @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime endTime,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {
        return service.findStats(startTime, endTime, uris, unique);
    }
}
