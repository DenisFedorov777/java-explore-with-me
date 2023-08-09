package ru.practicum.main.comments.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@ToString
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    Long id;
    @Column(nullable = false)
    String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "event_id")
    Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "author_id", updatable = false)
    User author;
    @Column(name = "created_date", nullable = false, updatable = false)
    LocalDateTime created = LocalDateTime.now();
    @Column(name = "updated_date")
    LocalDateTime updateTime = null; // время редактирования, default=null
    @Column(name = "isupdated")
    boolean isUpdated = false; //флаг показывает что комментарий был отредактирован, default=false
}