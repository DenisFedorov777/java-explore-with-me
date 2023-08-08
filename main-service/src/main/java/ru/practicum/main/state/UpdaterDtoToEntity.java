package ru.practicum.main.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.main.categories.repository.CategoryRepository;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.dto.UpdateEventRequest;
import ru.practicum.main.exceptions.InvalidDataException;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdaterDtoToEntity {
    private final CategoryRepository categoryRepository;

    public void fromDtoToEntity(UpdateEventRequest dto, Event event) {
        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            event.setCategory(categoryRepository.getReferenceById(dto.getCategory()));
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction() != null) {
            updateStatusState(dto, event);
        }
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            event.setTitle(dto.getTitle());
        }
    }

    private void updateStatusState(UpdateEventRequest dto, Event event) {
        switch (dto.getStateAction()) {
            case PUBLISH_EVENT:
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case SEND_TO_REVIEW:
                event.setState(State.PENDING);
                break;
            case REJECT_EVENT:
                if (event.getState().equals(State.PUBLISHED)) {
                    log.error("Мероприятие опубликовано и не может быть отклонено.");
                    throw new InvalidDataException("Мероприятие опубликовано и не может быть отклонено.");
                }
                event.setState(State.CANCELED);
                break;
            case CANCEL_REVIEW:
                event.setState(State.CANCELED);
                break;
        }
    }
}
