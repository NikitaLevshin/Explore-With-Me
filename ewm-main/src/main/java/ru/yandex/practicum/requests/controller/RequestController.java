package ru.yandex.practicum.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.requests.dto.ParticipationRequestDto;
import ru.yandex.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{userId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable int userId) {
        return requestService.getEventRequests(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createEventRequest(@PathVariable int userId,
                                                      @RequestParam int eventId) {
        return requestService.createEventRequest(userId, eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelEventRequest(@PathVariable int userId,
                                                      @PathVariable int requestId) {
        return requestService.cancelEventRequest(userId, requestId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable int userId,
                                                     @PathVariable int eventId) {
        return requestService.getRequests(userId, eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@PathVariable int userId,
                                                              @PathVariable int eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return requestService.updateRequest(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
