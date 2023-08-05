package ru.yandex.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.StatsClient;
import ru.yandex.practicum.events.dto.EventFullDto;
import ru.yandex.practicum.events.dto.EventShortDto;
import ru.yandex.practicum.events.mapper.EventMapper;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.model.EventSort;
import ru.yandex.practicum.events.model.EventStatus;
import ru.yandex.practicum.events.repository.EventRepository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.yandex.practicum.utils.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllEvents(String text, List<Integer> categories, Boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                            Boolean onlyAvailable, EventSort sort, Integer from, Integer size,
                                            HttpServletRequest httpServletRequest) {
        log.info("Запрос на получение всех событий");
        Pageable pageable;
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ValidationException("Старт после конца");
            }
        } else {
            rangeStart = LocalDateTime.now().minusYears(5);
            rangeEnd = LocalDateTime.now().plusYears(5);
        }
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    pageable = PageRequest.of(from / size, size, Sort.by("eventDate"));
                    break;
                case VIEWS:
                    pageable = PageRequest.of(from / size, size, Sort.by("views"));
                    break;
                default:
                    pageable = PageRequest.of(from / size, size);
            }
        } else {
            pageable = PageRequest.of(from / size, size);
        }
        statsClient.hit(httpServletRequest);
        if (onlyAvailable) {
            return eventRepository.findAll(text, categories, paid, rangeStart, rangeEnd, pageable)
                    .stream()
                    .map(e -> {
                        int views = getViews(e, httpServletRequest);
                        EventShortDto shortEvent = EventMapper.toEventShortDto(e);
                        shortEvent.setViews(views);
                        return shortEvent;
                    })
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAllWithoutState(text, categories, paid, rangeStart, rangeEnd, pageable)
                    .stream()
                    .map(e -> {
                        int views = getViews(e, httpServletRequest);
                        EventShortDto shortEvent = EventMapper.toEventShortDto(e);
                        shortEvent.setViews(views);
                        return shortEvent;
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getById(int id, HttpServletRequest httpServletRequest) {
        log.info("Запрос ивента с id {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event с таким id не найден"));
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new NotFoundException("Event с таким id не найден");
        }
        statsClient.hit(httpServletRequest);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setViews(getViews(event, httpServletRequest));
        return eventFullDto;
    }

    private int getViews(Event event, HttpServletRequest httpServletRequest) {
        List<String> uris = List.of(httpServletRequest.getRequestURI());
        ResponseEntity<Object> response = statsClient.stats(
                event.getCreatedOn().format(DATE_TIME_FORMATTER),
                event.getEventDate().format(DATE_TIME_FORMATTER),
                uris.toArray(new String[0]), true);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            String[] body = Objects.requireNonNull(response.getBody()).toString().split(" ");
            return Integer.parseInt(body[2].replaceAll("[\\D]", ""));
        } else {
            return 0;
        }
    }
}
