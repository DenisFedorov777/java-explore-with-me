package ru.practicum.main.exception;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
@Generated
@Slf4j
public class ErrorHandlers {
    private static final String ERROR_REASON_BAD_REQUEST = "Incorrectly made request.";

    @ExceptionHandler(InvalidTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSInvalidTimeException(final InvalidTimeException e) {
        log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                ERROR_REASON_BAD_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason(ERROR_REASON_BAD_REQUEST)
                .message("Error validation Data")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodBadRequestParamException(final MissingServletRequestParameterException e) {
        log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason(ERROR_REASON_BAD_REQUEST)
                .message("Error validation Data")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason(e.getMessage())
                .message(Arrays.toString(e.getStackTrace()))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
