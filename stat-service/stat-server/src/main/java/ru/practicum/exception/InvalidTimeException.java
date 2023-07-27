package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidTimeException extends RuntimeException {

    public InvalidTimeException(String message) {
        super(message);
        log.warn(message);
    }
}
