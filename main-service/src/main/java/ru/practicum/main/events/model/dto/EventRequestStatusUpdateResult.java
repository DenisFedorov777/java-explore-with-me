package ru.practicum.main.events.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.state.RequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateResult {

    @NotEmpty
    List<Long> requestIds;
    @NotNull
    RequestStatus status;
}
