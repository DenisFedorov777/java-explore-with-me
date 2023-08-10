package ru.practicum.main.events.model.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.categories.model.dto.CategoryDto;
import ru.practicum.main.state.Location;
import ru.practicum.main.state.State;
import ru.practicum.main.users.model.dto.UserShortDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {

    Long id;
    CategoryDto category;
    Long confirmedRequests;
    @JsonUnwrapped
    EventDescriptionsDto eventDescriptions;
    @JsonUnwrapped
    EventDateTimeDto eventDates;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    State state;
    Long views;
    Long comments;
}
