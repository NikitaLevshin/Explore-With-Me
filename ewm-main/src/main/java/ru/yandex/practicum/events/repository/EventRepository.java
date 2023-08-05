package ru.yandex.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e " +
            "WHERE ((lower(e.annotation) LIKE concat('%',lower(:text),'%') " +
            "OR lower(e.description) LIKE concat('%',lower(:text),'%')) " +
            "OR :text IS NULL ) " +
            "AND (e.category.id IN (:category) OR :category IS NULL) " +
            "AND (e.paid IN (:paid) OR :paid IS NULL) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "AND (e.participantLimit != 0 AND e.confirmedRequests < e.participantLimit)")
    List<Event> findAll(String text, List<Integer> category, Boolean paid,
                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                        Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (e.initiator.id IN (:users) OR :users IS NULL) " +
            "AND (e.state IN (:states) OR :states IS NULL) " +
            "AND (e.category.id IN (:categories) OR :categories IS NULL) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) ")
    List<Event> findByAdminWithState(List<Integer> users, List<EventStatus> states, List<Integer> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((lower(e.annotation) LIKE concat('%',lower(:text),'%') " +
            "OR lower(e.description) LIKE concat('%',lower(:text),'%')) " +
            "OR :text IS NULL ) " +
            "AND (e.category.id IN (:category) OR :category IS NULL) " +
            "AND (e.paid IN (:paid) OR :paid IS NULL) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) ")
    List<Event> findAllWithoutState(String text,
                                    List<Integer> category,
                                    Boolean paid,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Pageable pageable);

    List<Event> findEventsByInitiator_Id(int userId, Pageable pageable);

    Event findEventByIdAndInitiator_Id(int eventId, int userId);

    Optional<Event> findByIdAndStateIs(int eventId, EventStatus state);
}
