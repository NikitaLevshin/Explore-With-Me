package ru.yandex.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.comments.dto.CommentDto;
import ru.yandex.practicum.comments.dto.NewCommentDto;
import ru.yandex.practicum.comments.dto.UserCommentDto;
import ru.yandex.practicum.comments.mapper.CommentMapper;
import ru.yandex.practicum.comments.model.Comment;
import ru.yandex.practicum.comments.repository.CommentRepository;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.model.EventStatus;
import ru.yandex.practicum.events.repository.EventRepository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventRepository eventRepository;

    @Override
    public CommentDto create(NewCommentDto comment, int userId, int eventId) {
        log.info("Запрос на создание комментария от пользователя {} к событию {}", userId, eventId);
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Такого события не существует"));
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new NotFoundException("Событие должно быть опубликовано");
        }
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.fromNewCommentDto(comment, event, user)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCommentDto> getAllByUser(int userId, int from, int size) {
        log.info("Запрос на получение всех комментариев пользователем {}", userId);
        userService.getUserById(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return commentRepository.findAllByAuthor_IdOrderByCreatedDesc(userId, pageable)
                .stream()
                .map(CommentMapper::toUserCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getById(int userId, int commentId) {
        log.info("Запрос на получение комментария от пользователя {} по id {}", userId, commentId);
        userService.getUserById(userId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Такого комментария не существует"));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto editComment(NewCommentDto commentDto, int commentId, int userId) {
        log.info("Запрос на изменение комментария от пользователя {} с id комментария {}", userId, commentId);
        userService.getUserById(userId);
        Comment oldComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Такого комментария не существует"));
        oldComment.setText(commentDto.getText());
        return CommentMapper.toCommentDto(commentRepository.save(oldComment));
    }

    @Override
    public void deleteComment(int id, int userId) {
        log.info("Запрос на удаление комментария от пользователя {} с id комментария {}", userId, id);
        userService.getUserById(userId);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такого комментария не существует"));
        commentRepository.deleteById(id);
    }
}
