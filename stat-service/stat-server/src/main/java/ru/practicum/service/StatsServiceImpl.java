package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.InvalidTimeException;
import ru.practicum.repository.StatsRepository;
import ru.rpacticum.EndpointHitDto;
import ru.rpacticum.ViewStatsDto;

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
        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeException("Время начала должно быть раньше времени окончания!");
        }
        if (uri == null || uri.isEmpty()) {
            if (unique) {
                return repository.findAllStatsUniqueIp(startTime, endTime);
            } else {
                return repository.findAllStats(startTime, endTime);
            }
        } else {
            if (unique) {
                return repository.findStatsByUriUniqueIp(startTime, endTime, uri);
            } else {
                return repository.findAllStatsByUri(startTime, endTime, uri);
            }
        }
    }
}
