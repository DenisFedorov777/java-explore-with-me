package ru.practicum.main.compilations.service;

import lombok.experimental.UtilityClass;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.compilations.model.dto.CompilationDto;
import ru.practicum.main.compilations.model.dto.NewCompilationDto;
import ru.practicum.main.events.service.EventMapper;

import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
    }

    public CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toShortDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
