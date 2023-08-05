package ru.yandex.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.requests.model.RequestEventStatus;

@Data
@Builder
public class ParticipationRequestDto {
    private int id;
    private String created;
    private int event;
    private int requester;
    private RequestEventStatus status;
}
