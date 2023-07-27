package ru.practicum.service;

import ru.practicum.model.Stat;
import ru.rpacticum.EndpointHitDto;

public class StatsMapper {

    public static Stat toStat(EndpointHitDto endpoint) {
        return Stat.builder()
                .app(endpoint.getApp())
                .uri(endpoint.getUri())
                .ip(endpoint.getIp())
                .timestamp(endpoint.getTimestamp())
                .build();
    }
}
