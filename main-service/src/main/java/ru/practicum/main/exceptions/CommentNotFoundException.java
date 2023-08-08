package ru.practicum.main.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String message) {
        super(message);
        log.warn(message);
    }
}
