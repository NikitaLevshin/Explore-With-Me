package ru.yandex.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.categories.mapper.CategoryMapper;
import ru.yandex.practicum.categories.model.Category;
import ru.yandex.practicum.categories.service.PublicCategoryService;
import ru.yandex.practicum.events.dto.EventFullDto;
import ru.yandex.practicum.events.dto.EventShortDto;
import ru.yandex.practicum.events.dto.NewEventDto;
import ru.yandex.practicum.events.dto.UpdateEventUserRequest;
import ru.yandex.practicum.events.mapper.EventMapper;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.model.EventStatus;
import ru.yandex.practicum.events.repository.EventRepository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.exceptions.WrongArgumentException;
import ru.yandex.practicum.location.service.LocationService;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@ComponentScan()
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final PublicCategoryService categoryService;
    private final LocationService locationService;

    @Override
    public List<EventShortDto> getEvents(int userId, int from, int size) {
        log.info("Запрос на получение всех ивентов от пользователя {}", userId);
        return eventRepository.findEventsByInitiator_Id(
                        userId, PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        log.info("Запрос на создание ивента от пользователя с id {}", userId);
        Duration eventDuration = Duration.between(
                LocalDateTime.now(), newEventDto.getEventDate());
        if (eventDuration.toMinutes() < Duration.ofMinutes(120).toMinutes()) {
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше," +
                    " чем через два часа от текущего момента");
        }
        User user = userService.getUserById(userId);
        Category category = CategoryMapper.fromCategoryDto(categoryService.findCategoryById(newEventDto.getCategory()));
        locationService.saveLocation(newEventDto.getLocation());
        Event event = EventMapper.fromEventDto(newEventDto, category, user);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEvent(int userId, int eventId) {
        log.info("Запрос на получение ивента с id {} от пользователя {}", eventId, userId);
        userService.getUserById(userId);
        return EventMapper.toEventFullDto(eventRepository.findEventByIdAndInitiator_Id(eventId, userId));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.info("Запрос на обновление ивента с id {} от пользователя {}", eventId, userId);
        userService.getUserById(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Event event = getById(eventId);
        if (event.getState().equals(EventStatus.PENDING) || event.getState().equals(EventStatus.CANCELED)) {
            if (updateEventUserRequest.getEventDate() != null) {
                Duration eventDuration = Duration.between(
                        LocalDateTime.now(), LocalDateTime.parse(updateEventUserRequest.getEventDate(), formatter));
                if (eventDuration.toMinutes() < Duration.ofMinutes(120).toMinutes()) {
                    throw new ValidationException("Дата и время на которые намечено событие не может быть раньше," +
                            " чем через два часа от текущего момента");
                }
                event.setEventDate(LocalDateTime.parse(updateEventUserRequest.getEventDate()));
            }
            if (updateEventUserRequest.getAnnotation() != null) {
                event.setAnnotation(updateEventUserRequest.getAnnotation());
            }
            if (updateEventUserRequest.getCategory() != null) {
                event.setCategory(CategoryMapper.fromCategoryDto(updateEventUserRequest.getCategory()));
            }
            if (updateEventUserRequest.getDescription() != null) {
                event.setDescription(updateEventUserRequest.getDescription());
            }
            if (updateEventUserRequest.getLocation() != null) {
                event.setLocation(updateEventUserRequest.getLocation());
            }
            if (updateEventUserRequest.getPaid() != null) {
                event.setPaid(updateEventUserRequest.getPaid());
            }
            if (updateEventUserRequest.getParticipantLimit() != null) {
                event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
            }
            if (updateEventUserRequest.getRequestModeration() != null) {
                event.setRequestModeration(updateEventUserRequest.getRequestModeration());
            }
            if (updateEventUserRequest.getStateAction() != null) {
                switch (updateEventUserRequest.getStateAction()) {
                    case SEND_TO_REVIEW:
                        event.setState(EventStatus.PENDING);
                        break;
                    case CANCEL_REVIEW:
                        event.setState(EventStatus.CANCELED);
                        break;
                }
            }
            if (updateEventUserRequest.getTitle() != null) {
                event.setTitle(updateEventUserRequest.getTitle());
            }
            return EventMapper.toEventFullDto(eventRepository.save(event));
        } else {
            throw new WrongArgumentException("Ошибка статуса ивента");
        }
    }

    @Override
    public Event getById(int eventId) {
        log.info("Запрос ивента с id {}", eventId);
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event с таким id не найден"));
    }
}
