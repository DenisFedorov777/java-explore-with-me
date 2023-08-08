package ru.practicum.main.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.state.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Page<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Event findByIdAndInitiator_Id(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long eventId, State state);

    List<Event> findAllByIdIn(List<Long> eventIds);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    @Query("select min(e.publishedOn) " +
            "from Event as e")
    Optional<LocalDateTime> getMinDateInEvents();

    List<Event> findByCategoryId(Long categoryId);
}
