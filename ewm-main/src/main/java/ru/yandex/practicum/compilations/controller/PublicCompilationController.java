package ru.yandex.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilations.dto.CompilationDto;
import ru.yandex.practicum.compilations.service.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {

    private final PublicCompilationService publicCompilationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) String pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        return publicCompilationService.getCompilations(pinned, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{compId}")
    public CompilationDto getById(@PathVariable int compId) {
        return publicCompilationService.getById(compId);
    }

}
