package ru.yandex.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stats")
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String app;
    private String uri;
    private String ip;
    @Column(name = "creation_date")
    LocalDateTime creationDate;
}
