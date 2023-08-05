package ru.yandex.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.categories.mapper.CategoryMapper;
import ru.yandex.practicum.categories.service.PublicCategoryService;
import ru.yandex.practicum.events.dto.EventFullDto;
import ru.yandex.practicum.events.dto.UpdateEventAdminRequest;
import ru.yandex.practicum.events.mapper.EventMapper;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.model.EventStatus;
import ru.yandex.practicum.events.model.StateActionAdmin;
import ru.yandex.practicum.events.repository.EventRepository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.exceptions.WrongArgumentException;
import ru.yandex.practicum.exceptions.WrongStateException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.utils.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final PublicCategoryService publicCategoryService;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findAllEvents(List<Integer> users, List<EventStatus> states, List<Integer> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Запрос на получение всех событий администратором");
        Pageable pageable = PageRequest.of(from / size, size);
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ValidationException("Старт после конца");
            }
        } else {
            rangeStart = LocalDateTime.now().minusYears(5);
            rangeEnd = LocalDateTime.now().plusYears(5);
        }
        return eventRepository.findByAdminWithState(users, states, categories, rangeStart, rangeEnd, pageable)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(int id, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Запрос на обновление ивента администратором");
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event с таким id не найден"));
        if (event.getState() == EventStatus.PUBLISHED || event.getState() == EventStatus.CANCELED) {
            throw new WrongArgumentException("Событие уже опубликовано или отменено");
        }
        if (event.getState() != EventStatus.PENDING) {
            throw new ValidationException("Неверный статус ивента");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(EventStatus.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }

            if (updateEventAdminRequest.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(EventStatus.CANCELED);
            }
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(
                    CategoryMapper.fromCategoryDto(publicCategoryService.findCategoryById(updateEventAdminRequest.getCategory())));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(),DATE_TIME_FORMATTER);
            if (newEventDate.isAfter(event.getCreatedOn().plusHours(1))) {
                event.setEventDate(newEventDate);
            } else {
                throw new WrongStateException("Неверная дата начала события или неверный статус");
            }
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.getLocation().setLat(updateEventAdminRequest.getLocation().getLat());
            event.getLocation().setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle()  != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        int views = 0;
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }
}
