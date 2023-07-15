package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.model.Stats;
import ru.yandex.practicum.RequestDto;
import ru.yandex.practicum.ResponseDto;

import java.time.LocalDateTime;

@UtilityClass
public class StatsMapper {

    public static Stats fromStatsDto(RequestDto requestDto) {
        LocalDateTime localDateTime;
        if (requestDto.getCreationDate() == null) {
            localDateTime = LocalDateTime.now();
        } else {
            localDateTime = requestDto.getCreationDate();
        }
        return Stats.builder()
                .app(requestDto.getApp())
                .uri(requestDto.getUri())
                .ip(requestDto.getIp())
                .creationDate(localDateTime)
                .build();
    }

    public static ResponseDto toStatsDto(Stats stats) {
        return ResponseDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .build();
    }
}
