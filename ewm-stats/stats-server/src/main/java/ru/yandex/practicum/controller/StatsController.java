package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.RequestDto;
import ru.yandex.practicum.ResponseDto;
import ru.yandex.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseDto hit(@RequestBody RequestDto requestDto) {
        return statsService.hit(requestDto);
    }

    @GetMapping("/stats")
    public List<ResponseDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
