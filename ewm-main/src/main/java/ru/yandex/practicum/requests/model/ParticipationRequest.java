package ru.yandex.practicum.requests.model;

import lombok.*;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event eventsWithRequests;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    @NotNull
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestEventStatus status;
}
