package ru.practicum.main.exceptions;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
@Generated
@Slf4j
public class ErrorHandler {
    private static final String ERROR_REASON_BAD_REQUEST = "Incorrectly made request.";
    private static final String ERROR_REASON_CONFLICT = "Integrity constraint has been violated.";
    private static final String ERROR_REASON_NOT_FOUND = "The required object was not found.";

    @ExceptionHandler({InvalidValidationException.class, BadRequestException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDataException(final RuntimeException e) {
        log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                ERROR_REASON_BAD_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({PSQLException.class, InvalidDataException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSQLException(final RuntimeException e) {
        log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(
                HttpStatus.CONFLICT.name(),
                ERROR_REASON_CONFLICT,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            CategoryNotFoundException.class,
            CompilationNotFoundException.class,
            EventNotFoundException.class,
            RequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        log.error(e.getMessage(), Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                ERROR_REASON_NOT_FOUND,
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
