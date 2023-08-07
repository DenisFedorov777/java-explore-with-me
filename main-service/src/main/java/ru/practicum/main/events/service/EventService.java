package ru.practicum.main.events.service;

import ru.practicum.main.events.model.dto.*;
import ru.practicum.main.requests.ParticipationRequestDto;
import ru.practicum.main.state.EventSortType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getEvents(EventRequestDto requestDto);

    EventFullDto patchEventByAdmin(Long eventId, UpdateEventRequest request);

    List<EventShortDto> getEventsByUserId(Long userId, int from, int size);

    EventFullDto createEventByUserId(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto updateEventWithUserIdAndEventId(Long userId, Long eventId, UpdateEventRequest request);

    List<ParticipationRequestDto> getEventByParticipationByUser(Long userId, Long eventId);

    EventResponseStatusUpdateResult updateEventParticipationUsers(Long userId, Long eventId,
                                                                  EventRequestStatusUpdateResult request);

    List<EventShortDto> getEventsWithSortByText(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                boolean onlyAvailable,
                                                EventSortType sort,
                                                Integer from,
                                                Integer size,
                                                HttpServletRequest request);

    EventFullDto getEventById(Long id, HttpServletRequest request);
}
