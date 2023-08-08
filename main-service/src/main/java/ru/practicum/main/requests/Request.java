package ru.practicum.main.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.state.RequestStatus;
import ru.practicum.main.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id", nullable = false)
    @ToString.Exclude
    User requester;
    @Enumerated(EnumType.STRING)
    RequestStatus status;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    @ToString.Exclude
    Event event;
    @Column(name = "created_date")
    LocalDateTime created;
}
