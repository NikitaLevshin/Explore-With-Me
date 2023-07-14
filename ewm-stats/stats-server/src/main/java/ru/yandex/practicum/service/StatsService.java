package ru.yandex.practicum.service;

import ru.yandex.practicum.RequestDto;
import ru.yandex.practicum.ResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    ResponseDto hit(RequestDto requestDto);

    List<ResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique);
}
