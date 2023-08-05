package ru.yandex.practicum.events.service;

import ru.yandex.practicum.events.dto.EventFullDto;
import ru.yandex.practicum.events.dto.EventShortDto;
import ru.yandex.practicum.events.dto.NewEventDto;
import ru.yandex.practicum.events.dto.UpdateEventUserRequest;
import ru.yandex.practicum.events.model.Event;

import java.util.List;

public interface PrivateEventService {

    List<EventShortDto> getEvents(int userId, int from, int size);

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    EventFullDto getEvent(int userId, int eventId);

    EventFullDto updateEvent(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    Event getById(int eventId);
}
