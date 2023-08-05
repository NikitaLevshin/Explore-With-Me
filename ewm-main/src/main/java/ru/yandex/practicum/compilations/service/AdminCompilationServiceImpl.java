package ru.yandex.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.compilations.dto.CompilationDto;
import ru.yandex.practicum.compilations.dto.NewCompilationDto;
import ru.yandex.practicum.compilations.dto.UpdateCompilationRequestDto;
import ru.yandex.practicum.compilations.mapper.CompilationMapper;
import ru.yandex.practicum.compilations.model.Compilation;
import ru.yandex.practicum.compilations.repository.CompilationRepository;
import ru.yandex.practicum.events.model.Event;
import ru.yandex.practicum.events.repository.EventRepository;
import ru.yandex.practicum.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Запрос на создание подборки ивентов");
        List<Event> eventList = new ArrayList<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            eventList = eventRepository.findAllById(newCompilationDto.getEvents());
            if (eventList.size() != newCompilationDto.getEvents().size()) {
                throw new NotFoundException("Ивенты не найдены");
            }
        }
        return CompilationMapper.toCompilationDto(
                compilationRepository.save(CompilationMapper.fromCompilationDto(newCompilationDto, eventList)));
    }

    @Override
    @Transactional
    public void deleteCompilation(int id) {
        log.info("Запрос на удаление подборки с id {}", id);
        compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Такой подборки не найдено"));
        compilationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(int id, UpdateCompilationRequestDto updateCompilationRequestDto) {
        log.info("Запрос на обновление подборки с id {}", id);
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Такой подборки не найдено"));
        if (updateCompilationRequestDto.getEvents() != null && !updateCompilationRequestDto.getEvents().isEmpty()) {
            List<Event> eventList = eventRepository.findAllById(updateCompilationRequestDto.getEvents());
            if (eventList.size() != updateCompilationRequestDto.getEvents().size()) {
                throw new NotFoundException("Ивенты не найдены");
            }
            compilation.setEvents(eventList);
        }
        if (updateCompilationRequestDto.getPinned() != null) {
            compilation.setPinned(updateCompilationRequestDto.getPinned());
        }
        if (updateCompilationRequestDto.getTitle() != null) {
            compilation.setTitle(updateCompilationRequestDto.getTitle());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }
}
