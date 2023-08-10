package ru.yandex.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.comments.dto.CommentDto;
import ru.yandex.practicum.comments.mapper.CommentMapper;
import ru.yandex.practicum.comments.repository.CommentRepository;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.model.EventStatus;
import ru.yandex.practicum.events.repository.EventRepository;
import ru.yandex.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCommentServiceImpl implements PublicCommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;


    @Override
    public List<CommentDto> getAllByEvent(int eventId, int from, int size) {
        log.info("Запрос на получение всех комментариев к событию {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такого события не существует"));
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new NotFoundException("Event с таким id не найден");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return commentRepository.findAllByEvent_IdOrderByCreatedDesc(eventId, pageable)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
