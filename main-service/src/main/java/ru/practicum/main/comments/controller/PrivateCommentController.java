package ru.practicum.main.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comments.model.dto.CommentDto;
import ru.practicum.main.comments.service.CommentService;
import ru.practicum.main.comments.service.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto createdComment(@PathVariable(value = "userId") Long userId,
                                     @RequestParam(value = "eventId") Long eventId,
                                     @Validated @RequestBody CommentDto commentDto) {
        log.info("Creating comment {} by user Id {} and event Id {}", commentDto, userId, eventId);
        return commentService.saveComment(userId, eventId, commentDto);
    }

    @PatchMapping("/comments/{commentId}")
    @Validated(Marker.OnUpdate.class)
    public CommentDto updateComment(@PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "commentId") Long commentId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("Updating comment {}. Parameters: userId {} and comment Id {}", commentDto, userId, commentId);
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable(value = "userId") Long userId,
                                     @PathVariable(value = "commentId") Long commentId) {
        log.info("Get comments by Id {} and user Id {}", commentId, userId);
        return commentService.getCommentById(userId, commentId);
    }

    @GetMapping("/comments")
    public Collection<CommentDto> getUserComments(@PathVariable(value = "userId") Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get comments by user Id {}", userId);
        return commentService.getCommentsByUserId(userId, from, size);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(value = "userId") Long userId,
                              @PathVariable(value = "commentId") Long commentId) {
        log.info("Delete comments by Id {} and user Id {}", commentId, userId);
        commentService.deleteCommentById(userId, commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    public Collection<CommentDto> getCommentsByEventId(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "eventId") Long eventId,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Get comments by user Id {} and event id {}", userId, eventId);
        return commentService.getCommentsByEventId(userId, eventId, from, size);
    }
}