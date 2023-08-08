package ru.practicum.main.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.state.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventId(Long eventId);

    @Query("select r " +
            "from Request as r " +
            "where r.status = :status and r.event.id = :eventId ")
    List<Request> findConfirmedRequest(RequestStatus status, Long eventId);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByRequester_Id(Long requestId);

    @Query("select case when count(r) > 0 then true else false end " +
            "from Request as r " +
            "where r.event.id = :eventId and r.requester.id = :requesterId")
    boolean checkRepeatedRequest(Long eventId, Long requesterId);
}
