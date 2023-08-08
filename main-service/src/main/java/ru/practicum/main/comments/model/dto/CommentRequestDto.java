package ru.practicum.main.comments.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequestDto {

    Long userId;
    Long eventId;
    Integer from;
    Integer size;
}
