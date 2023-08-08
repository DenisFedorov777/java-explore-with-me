package ru.practicum.main.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void save(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uri, Boolean unique);
}
