package ru.practicum.main.compilations.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.events.model.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    List<EventShortDto> events;
    Long id;
    Boolean pinned;
    String title;
}