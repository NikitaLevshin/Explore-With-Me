package ru.yandex.practicum.requests.service;

import ru.yandex.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getEventRequests(int userId);

    ParticipationRequestDto createEventRequest(int userId, int eventId);

    ParticipationRequestDto cancelEventRequest(int userId, int requestId);

    List<ParticipationRequestDto> getRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequest(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
