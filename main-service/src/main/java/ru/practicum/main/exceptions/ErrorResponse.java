package ru.practicum.main.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.state.TimeConstant;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Generated
@Builder
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    @JsonFormat(pattern = TimeConstant.DATE_FORMAT)
    private LocalDateTime timestamp;
}
