package ru.practicum.main.comments.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.comments.model.Comment;
import ru.practicum.main.comments.model.dto.CommentDto;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.users.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment mapToComment(User user, Event event, CommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setEvent(event);
        comment.setAuthor(user);
        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setEventId(comment.getEvent().getId());
        dto.setCreated(comment.getCreated());
        dto.setUpdateTime(comment.getUpdateTime());
        dto.setUpdated(comment.isUpdated());
        return dto;
    }
}