package ru.yandex.practicum.comments.service;

import ru.yandex.practicum.comments.dto.CommentDto;
import ru.yandex.practicum.comments.dto.NewCommentDto;
import ru.yandex.practicum.comments.dto.UserCommentDto;

import java.util.List;

public interface PrivateCommentService {

    CommentDto create(NewCommentDto comment, int userId, int eventId);

    List<UserCommentDto> getAllByUser(int userId, int from, int size);

    CommentDto getById(int commentId, int userId);

    CommentDto editComment(NewCommentDto commentDto, int commentId, int userId);

    void deleteComment(int id, int userId);
}
