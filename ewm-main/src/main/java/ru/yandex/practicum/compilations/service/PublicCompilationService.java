package ru.yandex.practicum.compilations.service;

import ru.yandex.practicum.compilations.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getCompilations(String pinned, int from, int size);

    CompilationDto getById(int id);
}
