package ru.practicum.main.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.model.dto.EventFullDto;
import ru.practicum.main.events.model.dto.EventShortDto;
import ru.practicum.main.events.service.EventService;
import ru.practicum.main.state.EventSortType;
import ru.practicum.main.state.TimeConstant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventsControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto>
    getEventsWithSortByText(@RequestParam(required = false) String text,
                            @RequestParam(required = false) List<Long> categories,
                            @RequestParam(required = false) Boolean paid,
                            @RequestParam(required = false) @DateTimeFormat(pattern = TimeConstant.DATE_FORMAT) LocalDateTime rangeStart,
                            @RequestParam(required = false) @DateTimeFormat(pattern = TimeConstant.DATE_FORMAT) LocalDateTime rangeEnd,
                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                            @RequestParam(required = false) EventSortType sort,
                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                            @RequestParam(defaultValue = "10") @Positive Integer size,
                            HttpServletRequest request) {
        log.info("Тест который достал валиться, покажись!!!");
        return eventService.getEventsWithSortByText(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("получить событие по идентификатору: {}", id);
        return eventService.getEventById(id, request);
    }
}
