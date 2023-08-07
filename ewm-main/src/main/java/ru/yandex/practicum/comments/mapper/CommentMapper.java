package ru.yandex.practicum.comments.mapper;

import ru.yandex.practicum.comments.dto.CommentDto;
import ru.yandex.practicum.comments.dto.NewCommentDto;
import ru.yandex.practicum.comments.dto.UserCommentDto;
import ru.yandex.practicum.comments.model.Comment;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

    }

    public static Comment fromCommentDto(CommentDto commentDto, Event event, User author) {
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(author)
                .created(commentDto.getCreated())
                .build();
    }

    public static Comment fromNewCommentDto(NewCommentDto comment, Event event, User author) {
        return Comment.builder()
                .text(comment.getText())
                .event(event)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }

    public static UserCommentDto toUserCommentDto(Comment comment) {
        return UserCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .created(comment.getCreated())
                .build();
    }
}
