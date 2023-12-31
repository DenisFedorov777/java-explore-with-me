package ru.practicum.main.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
        printStackTrace();
        log.warn(message);
    }
}
