package ru.practicum.main.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.comments.model.dto.CommentDto;
import ru.practicum.main.comments.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicCommentController {

    private final CommentService service;

    @GetMapping("/comments/{eventId}")
    public Collection<CommentDto> getCommentsByEvent(@PathVariable(value = "eventId") Long eventId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get comments by event id {}", eventId);
        return service.getComments(eventId, from, size);
    }
}
