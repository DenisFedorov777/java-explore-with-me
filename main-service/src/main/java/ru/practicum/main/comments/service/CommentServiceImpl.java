package ru.practicum.main.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.comments.model.Comment;
import ru.practicum.main.comments.model.dto.CommentDto;
import ru.practicum.main.comments.model.dto.CommentRequestDto;
import ru.practicum.main.comments.repository.CommentRepository;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.exceptions.CommentNotFoundException;
import ru.practicum.main.exceptions.EventNotFoundException;
import ru.practicum.main.exceptions.InvalidDataException;
import ru.practicum.main.exceptions.UserNotFoundException;
import ru.practicum.main.users.model.User;
import ru.practicum.main.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.comments.service.CommentMapper.mapToComment;
import static ru.practicum.main.comments.service.CommentMapper.mapToCommentDto;
import static ru.practicum.main.state.Pagination.patternPageable;
import static ru.practicum.main.state.State.PUBLISHED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private Comment getByIdAndAuthorId(Long commentId, Long userId) {
        return commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id: " + commentId + " was not found."));
    }

    private Comment getById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id: " + commentId + " was not found."));
    }

    @Transactional
    @Override
    public CommentDto saveComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " was not found"));
        // нельзя добавить комментарий в неопубликованном событии
        if (!event.getState().equals(PUBLISHED)) {
            throw new InvalidDataException("Event not published.");
        }
        Comment comment = commentRepository.save(mapToComment(user, event, commentDto));
        log.info("Сохранение комментария.");
        return mapToCommentDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId, Integer from, Integer size) {
        log.info("Получение комментариев по идентификатору пользователя.");
        Pageable pagination = patternPageable(from, size);
        return commentRepository.findAllByAuthorId(userId, pagination).stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long userId, Long commentId, CommentDto dto) {
        Comment comment = getByIdAndAuthorId(commentId, userId);
        comment.setText(dto.getText());
        comment.setUpdated(true);
        comment.setUpdateTime(LocalDateTime.now());
        log.info("Редактирование комментария.");
        return mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getCommentById(Long userId, Long commentId) {
        Comment comment = getByIdAndAuthorId(commentId, userId);
        log.info("Получение комментария.");
        return mapToCommentDto(comment);
    }

    @Transactional
    @Override
    public void deleteCommentById(Long userId, Long commentId) {
        getByIdAndAuthorId(commentId, userId);
        commentRepository.deleteById(commentId);
        log.info("Удаление комментария автором.");
    }

    @Override
    public List<CommentDto> getCommentsByEventId(Long userId, Long eventId) {
        eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException("Event with id: " + eventId + " was not found"));
        return commentRepository.findAllByEventId(eventId).stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto updateCommentAdmin(Long commentId, CommentDto commentDto) {
        Comment comment = getById(commentId);
        comment.setText(commentDto.getText());
        log.info("Модерация комментария администратором.");
        return mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getCommentByIdAdmin(Long commentId) {
        Comment comment = getById(commentId);
        log.info("Получение комментария администратором по идентификатору.");
        return mapToCommentDto(comment);
    }

    @Transactional
    @Override
    public void deleteCommentByIdAdmin(Long commentId) {
        getById(commentId);
        commentRepository.deleteById(commentId);
        log.info("Удаление комментария администратором.");
    }

    @Override
    public List<CommentDto> getCommentsAdmin(CommentRequestDto requestDto) {
        log.info("Получение комментариев по выбранному условию.");
        Pageable pagination = patternPageable(requestDto.getFrom(), requestDto.getSize());
        return commentRepository.findAllByAuthorIdOrEventId(requestDto.getUserId(), requestDto.getEventId(), pagination)
                .stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }
}