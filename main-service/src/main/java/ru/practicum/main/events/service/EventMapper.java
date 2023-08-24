package ru.practicum.main.events.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.categories.service.CategoryMapper;
import ru.practicum.main.events.model.dto.EventResponseStatusUpdateResult;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.dto.*;
import ru.practicum.main.requests.ParticipationRequestDto;
import ru.practicum.main.state.RequestStatus;
import ru.practicum.main.users.service.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEvent(NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location(dto.getLocation())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDtoCategory(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .comments(event.getComments())
                .build();
    }

    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .category(event.getCategory() != null ? CategoryMapper.toDtoCategory(event.getCategory()) : null)
                .confirmedRequests(event.getConfirmedRequests())
                .eventDescriptions(EventDescriptionsDto.builder()
                        .annotation(event.getAnnotation())
                        .description(event.getDescription())
                        .title(event.getTitle())
                        .build())
                .eventDates(EventDateTimeDto.builder()
                        .createdOn(event.getCreatedOn())
                        .eventDate(event.getEventDate())
                        .publishedOn(event.getPublishedOn())
                        .build())
                .initiator(event.getInitiator() != null ? UserMapper.toShortDto(event.getInitiator()) : null)
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .views(event.getViews())
                .comments(event.getComments())
                .build();
    }

    public static EventResponseStatusUpdateResult toDtoResponseStatus(List<ParticipationRequestDto> requestsDto) {
        return EventResponseStatusUpdateResult.builder()
                .confirmedRequests(requestsDto.stream()
                        .filter(stat -> stat.getStatus().equals(RequestStatus.CONFIRMED))
                        .collect(Collectors.toList()))
                .rejectedRequests(requestsDto.stream()
                        .filter(stat -> stat.getStatus().equals(RequestStatus.REJECTED))
                        .collect(Collectors.toList()))
                .build();
    }
}
