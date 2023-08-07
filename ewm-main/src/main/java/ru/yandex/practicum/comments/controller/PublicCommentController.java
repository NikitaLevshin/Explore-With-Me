package ru.yandex.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comments.dto.CommentDto;
import ru.yandex.practicum.comments.service.PublicCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {

    private final PublicCommentService publicCommentService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CommentDto> getAllByEvent(@RequestParam int eventId,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                   @RequestParam(defaultValue = "10")@Positive int size) {
        return publicCommentService.getAllByEvent(eventId, from, size);
    }
}
