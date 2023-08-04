package ru.practicum.main.events.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.StatsClient;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.categories.repository.CategoryRepository;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.dto.*;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.exceptions.BadRequestException;
import ru.practicum.main.exceptions.EventNotFoundException;
import ru.practicum.main.exceptions.InvalidDataException;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.requests.ParticipationRequestDto;
import ru.practicum.main.requests.Request;
import ru.practicum.main.requests.RequestMapper;
import ru.practicum.main.requests.RequestRepository;
import ru.practicum.main.state.EventSortType;
import ru.practicum.main.state.RequestStatus;
import ru.practicum.main.state.State;
import ru.practicum.main.state.UpdaterDtoToEntity;
import ru.practicum.main.users.model.User;
import ru.practicum.main.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.main.events.model.QEvent.event;
import static ru.practicum.main.state.Pagination.patternPageable;
import static ru.practicum.main.state.Pagination.patternPageableWithSort;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final UpdaterDtoToEntity update;

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<State> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        Integer from, Integer size) {
        Page<Event> result;
        BooleanBuilder bb = new BooleanBuilder();
        Pageable pagination = patternPageable(from, size);
        if (users != null) {
            bb.and(event.initiator.id.in(users));
        }
        if (states != null) {
            bb.and(event.state.in(states));
        }
        if (categories != null) {
            bb.and(event.category.id.in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            bb.and(event.eventDate.between(rangeStart, rangeEnd));
        }
        if (bb.getValue() != null) {
            result = repository.findAll(bb.getValue(), pagination);
        } else {
            result = repository.findAll(pagination);
        }
        updateViews(result.getContent());
        return result.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto patchEventByAdmin(Long eventId, UpdateEventRequest request) {
        Event event = validateExistsEvent(eventId);
        validatedTimeToStartEventByAdmin(request.getEventDate());
        validatePendingStatusForUpdate(event);
        update.fromDtoToEntity(request, event);
        updateViews(List.of(event));
        return EventMapper.toFullDto(repository.save(event));
    }


    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {
        validateExistsUser(userId);
        Pageable pagination = patternPageable(from, size);
        List<Event> events = repository.findAllByInitiator_Id(userId, pagination).getContent();
        updateViews(events);
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto createEventByUserId(Long userId, NewEventDto newEventDto) {
        validatedTimeToStartEvent(newEventDto.getEventDate());
        User initiator = validateExistsUser(userId);
        Category eventCategory = categoryRepository.getReferenceById(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto);
        setInitialParameters(initiator, eventCategory, event);
        return EventMapper.toFullDto(repository.save(event));
    }


    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        validateExistsUser(userId);
        validateExistsEvent(eventId);
        Event event = repository.findByIdAndInitiator_Id(eventId, userId);
        updateViews(List.of(event));
        return EventMapper.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEventWithUserIdAndEventId(Long userId, Long eventId, UpdateEventRequest request) {
        validateExistsUser(userId);
        validatedTimeToStartEvent(request.getEventDate());
        Event event = validateExistsEvent(eventId);
        validateEventStatusForUpdate(event);
        update.fromDtoToEntity(request, event);
        updateViews(List.of(event));
        return EventMapper.toFullDto(repository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getEventByParticipationByUser(Long userId, Long eventId) {
        validateExistsUser(userId);
        Event event = validateExistsEvent(eventId);
        isCheckUserInitiatorEvent(userId, event.getInitiator());
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toDtoRequest)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventResponseStatusUpdateResult updateEventParticipationUsers(Long userId, Long eventId,
                                                                         EventRequestStatusUpdateResult request) {
        validateExistsUser(userId);
        Event event = validateExistsEvent(eventId);
        isCheckUserInitiatorEvent(userId, event.getInitiator());
        List<Request> requests = getListWithStatusPendingAndUserInitiator(userId, request);
        List<Request> confirmedRequest = getConfirmedRequest(eventId);
        validateRequestsNotEmpty(requests);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            requests.forEach(req -> req.setStatus(RequestStatus.CONFIRMED));
            List<ParticipationRequestDto> requestsDto = requests.stream()
                    .map(RequestMapper::toDtoRequest)
                    .collect(Collectors.toList());
            return EventMapper.toDtoResponseStatus(requestsDto);
        }
        validateParticipantLimit(event, confirmedRequest);
        updateRequestsStatus(request, event, requests, confirmedRequest);
        saveRequestsData(event, requests);
        return EventMapper.toDtoResponseStatus(requests.stream()
                .map(RequestMapper::toDtoRequest)
                .collect(Collectors.toList()));
    }

    @Override
    public List<EventShortDto> getEventsWithSortByText(String text,
                                                       List<Long> categories,
                                                       Boolean paid, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd, boolean onlyAvailable,
                                                       EventSortType sort, Integer from, Integer size,
                                                       HttpServletRequest request) {
        Page<Event> resultEvents;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Pageable paginationWithSort = patternPageableWithSort(from, size, sort);

        if (text != null) {
            booleanBuilder.and(event.annotation.containsIgnoreCase(text))
                    .or(event.description.containsIgnoreCase(text));
        }
        if (categories != null) {
            booleanBuilder.and(event.category.id.in(categories));
        }
        if (paid != null) {
            booleanBuilder.and(event.paid.eq(paid));
        }
        if (rangeStart != null && rangeEnd != null) {
            booleanBuilder.and(event.eventDate.between(rangeStart, rangeEnd));
        }
        if (onlyAvailable) {
            booleanBuilder.and(event.participantLimit.eq(0L))
                    .or(event.participantLimit.gt(event.confirmedRequests));
        }
        if (booleanBuilder.getValue() != null) {
            resultEvents = repository.findAll(booleanBuilder.getValue(), paginationWithSort);
        } else {
            resultEvents = repository.findAll(paginationWithSort);
        }
        updateViews(resultEvents.getContent());
        statsClient.saveStat(request);
        return resultEvents.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = repository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException("Event with id was not found"));
        updateViews(List.of(event));
        statsClient.saveStat(request);
        return EventMapper.toFullDto(event);
    }

    private static void validateEventStatusForUpdate(Event event) {
        if (event.getState() != null && (event.getState().equals(State.PUBLISHED)
                || event.getState().equals(State.REJECTED))) {
            log.error("Only pending or canceled events can be changed");
            throw new InvalidDataException("Only pending or canceled events can be changed");
        }
    }

    private void isCheckUserInitiatorEvent(Long userId, User checkUser) {
        User user = userRepository.getReferenceById(userId);
        if (!user.equals(checkUser)) {
            log.info("User if not initiator this event");
            throw new InvalidDataException("User if not initiator this event");
        }
    }

    private User validateExistsUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id not found: " + userId));
    }

    private Event validateExistsEvent(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id was not found: " + eventId));
    }

    private void validatedTimeToStartEvent(LocalDateTime eventTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (eventTime != null && eventTime.isBefore(currentTime.plusHours(2))) {
            log.info("Event date {} is before current time {}", eventTime, currentTime);
            throw new BadRequestException("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. Value: " + eventTime);
        }
    }

    private static void validatePendingStatusForUpdate(Event event) {
        if (!event.getState().equals(State.PENDING)) {
            log.error("Cannot publish the event because it's not in the right state: PUBLISHED");
            throw new InvalidDataException("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
    }

    private static void setInitialParameters(User initiator, Category eventCategory, Event event) {
        event.setConfirmedRequests(0L);
        event.setViews(0L);
        event.setInitiator(initiator);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(eventCategory);
    }

    private void validatedTimeToStartEventByAdmin(LocalDateTime eventTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (eventTime != null && eventTime.isBefore(currentTime.plusHours(1))) {
            log.info("Event date {} is before current time {}", eventTime, currentTime);
            throw new BadRequestException("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. Value: " + eventTime);
        }
    }

    private void updateViews(List<Event> resultEvents) {
        List<Long> eventsIds = resultEvents.stream()
                .filter(e -> e.getPublishedOn() != null)
                .map(Event::getId)
                .collect(Collectors.toList());
        Optional<LocalDateTime> startDate = repository.getMinDateInEvents();
        if (startDate.isPresent()) {
            LocalDateTime start = startDate.get();
            LocalDateTime end = LocalDateTime.now();
            Long totalViews = statsClient.getViews(start, end, eventsIds);
            resultEvents.forEach(v -> v.setViews(totalViews));
        } else {
            resultEvents.forEach(v -> v.setViews(0L));
        }
    }

    private void saveRequestsData(Event event, List<Request> requests) {
        for (Request req : requests) {
            if (req.getStatus().equals(RequestStatus.CONFIRMED)) {
                event.setConfirmedRequests(+1L);
            }
        }
        repository.save(event);
    }

    private static void updateRequestsStatus(EventRequestStatusUpdateResult request, Event event, List<Request> requests, List<Request> confirmedRequest) {
        switch (request.getStatus()) {
            case REJECTED:
                requests.forEach(req -> req.setStatus(RequestStatus.REJECTED));
                break;
            case CONFIRMED:
                long limit = event.getParticipantLimit() - confirmedRequest.size();
                if (requests.size() <= limit) {
                    requests.forEach(req -> req.setStatus(RequestStatus.CONFIRMED));
                } else if (limit > 0) {
                    requests.subList(0, (int) (limit - 1))
                            .forEach((req -> req.setStatus(RequestStatus.CONFIRMED)));
                    requests.subList((int) limit, requests.size())
                            .forEach(req -> req.setStatus(RequestStatus.REJECTED));
                } else {
                    requests.forEach(req -> req.setStatus(RequestStatus.REJECTED));
                }
                break;
        }
    }

    private static void validateParticipantLimit(Event event, List<Request> confirmedRequest) {
        if (event.getParticipantLimit() - confirmedRequest.size() < 1) {
            log.error("The participant limit has been reached");
            throw new InvalidDataException("The participant limit has been reached");
        }
    }

    private List<Request> getConfirmedRequest(Long eventId) {
        return requestRepository.findConfirmedRequest(RequestStatus.CONFIRMED, eventId);
    }

    private List<Request> getListWithStatusPendingAndUserInitiator(Long userId, EventRequestStatusUpdateResult request) {
        User user = userRepository.getReferenceById(userId);
        return requestRepository.findAllByIdIn(request.getRequestIds()).stream()
                .filter(status -> status.getStatus().equals(RequestStatus.PENDING))
                .filter(initiator -> initiator.getEvent().getInitiator().equals(user))
                .collect(Collectors.toList());
    }

    private static void validateRequestsNotEmpty(List<Request> requests) {
        if (requests.isEmpty()) {
            log.error("It is not possible to cancel an already confirmed application");
            throw new InvalidDataException("It is not possible to cancel an already confirmed application");
        }
    }
}
