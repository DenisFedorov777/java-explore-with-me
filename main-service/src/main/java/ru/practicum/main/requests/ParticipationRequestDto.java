package ru.practicum.main.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.state.TimeConstant;
import ru.practicum.main.state.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeConstant.DATE_FORMAT)
    LocalDateTime created;
    Long event;
    Long requester;
    RequestStatus status;
}
