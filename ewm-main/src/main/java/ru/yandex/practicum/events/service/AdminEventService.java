package ru.yandex.practicum.events.service;

import ru.yandex.practicum.events.dto.EventFullDto;
import ru.yandex.practicum.events.dto.UpdateEventAdminRequest;
import ru.yandex.practicum.events.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventFullDto> findAllEvents(List<Integer> users, List<EventStatus> states, List<Integer> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(int id, UpdateEventAdminRequest updateEventAdminRequest);
}
