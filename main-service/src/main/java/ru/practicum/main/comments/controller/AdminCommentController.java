package ru.practicum.main.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comments.model.dto.CommentDto;
import ru.practicum.main.comments.model.dto.CommentRequestDto;
import ru.practicum.main.comments.service.CommentService;
import ru.practicum.main.comments.service.Marker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping
    public Collection<CommentDto> getComments(@RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) Long eventId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("Get comments by user id {} and event id {}", userId, eventId);
        return commentService.getCommentsAdmin(
                CommentRequestDto.builder()
                        .userId(userId)
                        .eventId(eventId)
                        .from(from)
                        .size(size)
                        .build()

        );
    }

    // удалить комментарий
    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCommentByIdAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("Delete comments by Id {}", commentId);
        commentService.deleteCommentByIdAdmin(commentId);
    }

    // отредактировать комментарий
    @PatchMapping("/{commentId}")
    @Validated(Marker.OnUpdate.class)
    public CommentDto updateCommentAdmin(@PathVariable(value = "commentId") Long commentId,
                                         @Valid @RequestBody CommentDto commentDto) {
        log.info("Updating comment {} by comment Id {}", commentDto, commentId);
        return commentService.updateCommentAdmin(commentId, commentDto);
    }

    // запросить комментарий по ид
    @GetMapping("/{commentId}")
    public CommentDto getCommentByIdAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("Get comment by Id {}", commentId);
        return commentService.getCommentByIdAdmin(commentId);
    }
}