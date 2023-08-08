package ru.practicum.main.events.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.state.TimeConstant;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDateTimeDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeConstant.DATE_FORMAT)
    LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeConstant.DATE_FORMAT)
    LocalDateTime eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeConstant.DATE_FORMAT)
    LocalDateTime publishedOn;
}
