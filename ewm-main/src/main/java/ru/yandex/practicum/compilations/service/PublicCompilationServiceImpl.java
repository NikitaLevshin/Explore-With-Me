package ru.yandex.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.compilations.dto.CompilationDto;
import ru.yandex.practicum.compilations.mapper.CompilationMapper;
import ru.yandex.practicum.compilations.repository.CompilationRepository;
import ru.yandex.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;


    @Override
    public List<CompilationDto> getCompilations(String pinned, int from, int size) {
        log.info("Запрос на получение подборок событий");
        return compilationRepository.findCompilationsByPinnedIs(Boolean.parseBoolean(pinned), PageRequest.of(from / size, size))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(int id) {
        log.info("Запрос на получение подборки с id {}", id);
        return CompilationMapper.toCompilationDto(compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборки с таким id не найдено")));
    }
}
