package ru.practicum.service;

import ru.rpacticum.EndpointHitDto;
import ru.rpacticum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void save(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uri, Boolean unique);
}
