package ru.yandex.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.requests.model.RequestEventStatus;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestEventStatus status;
}