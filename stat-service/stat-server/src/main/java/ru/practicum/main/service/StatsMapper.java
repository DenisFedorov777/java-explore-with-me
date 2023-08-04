package ru.practicum.main.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.main.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {

    public static Stat toStat(EndpointHitDto endpoint) {
        return Stat.builder()
                .app(endpoint.getApp())
                .uri(endpoint.getUri())
                .ip(endpoint.getIp())
                .timestamp(LocalDateTime.parse(endpoint.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
