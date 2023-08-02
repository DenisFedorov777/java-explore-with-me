package ru.practicum;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsRequestDto {

    final LocalDateTime start;
    final LocalDateTime end;
    final List<String> uris;
    final Boolean unique;
}
