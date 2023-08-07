package ru.yandex.practicum.comments.service;

import ru.yandex.practicum.comments.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {

    public List<CommentDto> getAllByEvent(int eventId, int from, int size);
}
