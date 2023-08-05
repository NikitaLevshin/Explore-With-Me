package ru.yandex.practicum.events.service;

import ru.yandex.practicum.events.dto.EventFullDto;
import ru.yandex.practicum.events.dto.EventShortDto;
import ru.yandex.practicum.events.model.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {

    List<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     Boolean onlyAvailable, EventSort sort, Integer from, Integer size,
                                     HttpServletRequest httpServletRequest);

    EventFullDto getById(int id, HttpServletRequest httpServletRequest);
}
