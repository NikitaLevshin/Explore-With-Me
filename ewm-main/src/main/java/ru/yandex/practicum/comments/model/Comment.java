package ru.yandex.practicum.comments.model;

import lombok.*;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String text;
    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;
    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;
    private LocalDateTime created;
}
