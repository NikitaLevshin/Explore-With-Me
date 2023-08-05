package ru.yandex.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.model.EventStatus;
import ru.yandex.practicum.events.repository.EventRepository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.WrongArgumentException;
import ru.yandex.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.requests.dto.ParticipationRequestDto;
import ru.yandex.practicum.requests.mapper.RequestMapper;
import ru.yandex.practicum.requests.model.ParticipationRequest;
import ru.yandex.practicum.requests.model.RequestEventStatus;
import ru.yandex.practicum.requests.repository.RequestRepository;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getEventRequests(int userId) {
        log.info("Запрос на получение пользователем {} запросов на участие в ивентах", userId);
        userService.getUserById(userId);
        return requestRepository.findParticipationRequestsByRequester_Id(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createEventRequest(int userId, int eventId) {
        log.info("Запрос на создание пользователем {} запроса на участие в ивенте", userId);
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такого события нет"));
        if (requestRepository.findAllByRequesterAndEventsWithRequests(user, event).isPresent()) {
            throw new WrongArgumentException("Подан повторный запрос на участие");
        }
        if (event.getInitiator().getId() == userId) {
            throw new WrongArgumentException("Подан запрос на участие в своем же событии");
        }
        if (event.getState() == EventStatus.PENDING || event.getState() == EventStatus.CANCELED) {
            throw new WrongArgumentException("Подан запрос в неопубликованном или отмененном событии");
        }
        if (event.getParticipantLimit() <= requestRepository.findAllByEventsWithRequests(event).size() &&
                event.getParticipantLimit() != 0) {
            throw new WrongArgumentException("Достигнут лимит участников");
        }
        if (event.isRequestModeration() && event.getParticipantLimit() != 0) {
            return RequestMapper.toParticipationRequestDto(
                    requestRepository.save(
                            ParticipationRequest.builder()
                                    .requester(user)
                                    .eventsWithRequests(event)
                                    .created(LocalDateTime.now())
                                    .status(RequestEventStatus.PENDING)
                                    .build()));
        } else {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            return RequestMapper.toParticipationRequestDto(
                    requestRepository.save(
                            ParticipationRequest.builder()
                                    .requester(user)
                                    .eventsWithRequests(event)
                                    .created(LocalDateTime.now())
                                    .status(RequestEventStatus.CONFIRMED)
                                    .build()));
        }
    }

    @Override
    public ParticipationRequestDto cancelEventRequest(int userId, int requestId) {
        log.info("Запрос на отмену пользователем {} запроса на участие в ивенте", userId);
        ParticipationRequest participationRequest = requestRepository.findParticipationRequestByIdAndRequester_Id(
                        requestId, userId)
                .orElseThrow(() -> new NotFoundException("Такой подборки нет"));
        participationRequest.setStatus(RequestEventStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(int userId, int eventId) {
        log.info("Запрос на получение пользователем {} запроса на участие в ивенте с id {}", userId, eventId);
        return requestRepository.findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequest(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Запрос на обновление пользователем {} запроса на участие в ивенте", userId);
        List<ParticipationRequest> requests =
                requestRepository.findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId);
        if (requests.isEmpty()) {
            throw new NotFoundException("Список участий в событиях пуст");
        }

        for (ParticipationRequest request : requests) {

            if (request.getEventsWithRequests().getParticipantLimit() == 0
                    || !request.getEventsWithRequests().isRequestModeration()) {
                request.setStatus(RequestEventStatus.CONFIRMED);
                request.getEventsWithRequests().setConfirmedRequests(request.getEventsWithRequests().getConfirmedRequests() + 1);
            } else if (request.getStatus().equals(RequestEventStatus.PENDING)) {
                if (request.getEventsWithRequests().getConfirmedRequests() >= request.getEventsWithRequests().getParticipantLimit()) {
                    request.setStatus(RequestEventStatus.REJECTED);
                } else {
                    request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                    if (request.getStatus().equals(RequestEventStatus.CONFIRMED)) {
                        request.getEventsWithRequests().setConfirmedRequests(request.getEventsWithRequests().getConfirmedRequests() + 1);
                    }
                }
            } else {
                throw new WrongArgumentException("Ошибка с статусе запроса");
            }
        }
        requestRepository.saveAll(requests);

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (request.getStatus().equals(RequestEventStatus.CONFIRMED)) {
                confirmedRequests.add(request);
            }
            if (request.getStatus().equals(RequestEventStatus.REJECTED)) {
                rejectedRequests.add(request);
            }
        }

        return RequestMapper.toEventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
