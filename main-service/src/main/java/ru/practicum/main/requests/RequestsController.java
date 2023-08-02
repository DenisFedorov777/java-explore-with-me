package ru.practicum.main.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class RequestsController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getParticipationInEventsByUserId(@PathVariable Long userId) {
        log.info("Get participation's by userId {}", userId);
        return requestService.getParticipationInEventsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequestByUserOnParticipation(@PathVariable Long userId,
                                                                      @RequestParam Long eventId) {
        log.info("Creating request by userId {} and eventId {}", userId, eventId);
        return requestService.createRequestByUserOnParticipation(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId,
                                                       @PathVariable Long requestId) {
        log.info("Cancelling requestId {} by userId {} ", requestId, userId);
        return requestService.cancelRequestByUser(userId, requestId);
    }
}
