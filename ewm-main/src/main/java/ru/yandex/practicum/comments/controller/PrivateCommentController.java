package ru.yandex.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comments.dto.CommentDto;
import ru.yandex.practicum.comments.dto.NewCommentDto;
import ru.yandex.practicum.comments.dto.UserCommentDto;
import ru.yandex.practicum.comments.service.PrivateCommentService;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class PrivateCommentController {

    private final PrivateCommentService privateCommentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/comments")
    public CommentDto create(@PathVariable int userId,
                             @RequestParam int eventId,
                             @RequestBody @Valid NewCommentDto comment) {
        return privateCommentService.create(comment, userId, eventId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/comments")
    List<UserCommentDto> getAllByUser(@PathVariable int userId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10")@Positive int size) {
        return privateCommentService.getAllByUser(userId, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/comments/{commentId}")
    CommentDto getById(@PathVariable int userId,
                       @PathVariable int commentId) {
        return privateCommentService.getById(userId, commentId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}/comments/{commentId}")
    CommentDto editComment(@PathVariable int userId,
                             @PathVariable int commentId,
                             @RequestBody @Valid NewCommentDto comment) {
        return privateCommentService.editComment(comment, commentId, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/comments/{commentId}")
    void deleteComment(@PathVariable int userId,
                       @PathVariable int commentId) {
        privateCommentService.deleteComment(commentId, userId);
    }
}
