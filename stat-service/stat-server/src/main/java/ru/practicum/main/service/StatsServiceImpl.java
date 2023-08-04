package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.main.exception.InvalidTimeException;
import ru.practicum.main.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void save(EndpointHitDto endpointHitDto) {
        repository.save(StatsMapper.toStat(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> findStats(LocalDateTime startTime, LocalDateTime endTime, List<String> uri, Boolean unique) {
        List<ViewStatsDto> dtos;
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeException("Время начала должно быть раньше времени окончания!");
        }
        if (uri == null || uri.isEmpty()) {
            if (unique) {
                dtos = repository.findAllStatsUniqueIp(startTime, endTime);
            } else {
                dtos = repository.findAllStats(startTime, endTime);
            }
        } else {
            if (unique) {
                dtos = repository.findStatsByUriUniqueIp(startTime, endTime, uri);
            } else {
                dtos = repository.findAllStatsByUri(startTime, endTime, uri);
            }
        }
        return dtos;
    }
}
