package ru.practicum.main.requests;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMapper {
    public ParticipationRequestDto toDtoRequest(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
