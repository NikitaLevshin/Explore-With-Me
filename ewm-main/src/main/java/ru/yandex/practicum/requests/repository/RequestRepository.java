package ru.yandex.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.requests.model.ParticipationRequest;
import ru.yandex.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    List<ParticipationRequest> findParticipationRequestsByRequester_Id(int userId);

    Optional<ParticipationRequest> findParticipationRequestByIdAndRequester_Id(int requestId, int userId);

    List<ParticipationRequest> findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(int eventId, int userId);

    Optional<ParticipationRequest> findAllByRequesterAndEventsWithRequests(User user, Event event);

    List<ParticipationRequest> findAllByEventsWithRequests(Event event);
}
