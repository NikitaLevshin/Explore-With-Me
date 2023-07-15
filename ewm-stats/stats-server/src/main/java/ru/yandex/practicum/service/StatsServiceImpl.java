package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.RequestDto;
import ru.yandex.practicum.ResponseDto;
import ru.yandex.practicum.exceptions.WrongTimeException;
import ru.yandex.practicum.mapper.StatsMapper;
import ru.yandex.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public ResponseDto hit(RequestDto requestDto) {
        log.info("Вызван метод hit");
        return StatsMapper.toStatsDto(statsRepository.save(StatsMapper.fromStatsDto(requestDto)));
    }

    @Override
    public List<ResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Вызван метод getStats");
        if (start.isAfter(end)) throw new WrongTimeException("Время старта не может быть позже времени окончания");
        if (uris != null && !uris.isEmpty()) {
            if (unique) {
                    return statsRepository.findByUniqueUri(start, end, uris);
            } else {
                    return statsRepository.findByUri(start, end, uris);
            }
        } else {
            if (unique) {
                return statsRepository.findUniqueStat(start, end);
            } else {
                return statsRepository.findStat(start, end);
            }
        }
    }
}
