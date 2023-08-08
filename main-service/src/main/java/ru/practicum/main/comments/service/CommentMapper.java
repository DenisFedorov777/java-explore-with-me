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
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getEvent().getId(),
                comment.getCreated()
        );
    }
}