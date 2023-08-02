package ru.practicum.main.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.exceptions.EventNotFoundException;
import ru.practicum.main.exceptions.InvalidDataException;
import ru.practicum.main.exceptions.RequestNotFoundException;
import ru.practicum.main.state.RequestStatus;
import ru.practicum.main.state.State;
import ru.practicum.main.users.model.User;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationInEventsByUserId(Long userId) {
        validateExistsUser(userId);
        return repository.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::toDtoRequest)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequestByUserOnParticipation(Long userId, Long eventId) {
        Request request = new Request();
        User userRequester = validateExistsUser(userId);
        Event event = validateExistsEvent(eventId);
        List<Request> confirmedRequests = repository.findConfirmedRequest(RequestStatus.CONFIRMED, eventId);
        validateCreateByUserRequest(userId, eventId, userRequester, event, confirmedRequests);
        updateRequestData(request, userRequester, event);
        return RequestMapper.toDtoRequest(repository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        User userRequester = validateExistsUser(userId);
        Request request = validateExistsRequest(requestId);
        validateUserOwnerRequest(userRequester, request.getRequester());
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDtoRequest(repository.save(request));
    }

    private static void validateUserOwnerRequest(User userOwnerRequest, User userRequester) {
        if (!userOwnerRequest.equals(userRequester)) {
            log.error("Only the user who created the request can cancel it.");
            throw new InvalidDataException("Only the user who created the request can cancel it.");
        }
    }

    private User validateExistsUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id not found: " + userId));
    }

    private Event validateExistsEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id was not found: " + eventId));
    }

    private Request validateExistsRequest(Long requestId) {
        return repository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request with id not found: " + requestId));
    }

    private static void updateRequestData(Request request, User userRequester, Event event) {
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        request.setCreated(LocalDateTime.now());
        request.setRequester(userRequester);
        request.setEvent(event);
    }

    private void validateCreateByUserRequest(Long userId, Long eventId, User userRequester, Event event,
                                             List<Request> confirmedRequests) {
        if (userRequester.equals(event.getInitiator())) {
            log.error("Initiator of the event cannot add a request to participate in his event");
            throw new InvalidDataException("Initiator of the event cannot add a request to participate in his event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.error("Can't participate in an unpublished event");
            throw new InvalidDataException("Can't participate in an unpublished event");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == confirmedRequests.size()) {
            log.error("Limit of requests for participation has been reached");
            throw new InvalidDataException("Limit of requests for participation has been reached");
        }
        boolean isCheckRepeatedRequest = repository.checkRepeatedRequest(eventId, userId);
        if (isCheckRepeatedRequest) {
            log.error("Can't add a repeat request");
            throw new InvalidDataException("Can't add a repeat request");
        }
    }
}
