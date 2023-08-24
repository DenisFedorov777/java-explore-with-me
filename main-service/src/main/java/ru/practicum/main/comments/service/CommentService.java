package ru.practicum.main.comments.service;

import ru.practicum.main.comments.model.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto saveComment(Long userId, Long eventId, CommentDto commentDto);

    List<CommentDto> getCommentsByUserId(Long userId, Integer from, Integer size);

    CommentDto updateComment(Long userId, Long commentId, CommentDto dto);

    CommentDto getCommentById(Long userId, Long commentId);

    void deleteCommentById(Long userId, Long commentId);

    List<CommentDto> getCommentsByEventId(Long userId, Long eventId, Integer from, Integer size);

    List<CommentDto> getComments(Long eventId, Integer from, Integer size);

    CommentDto updateCommentAdmin(Long commentId, CommentDto commentDto);

    void deleteCommentByIdAdmin(Long commentId);
}