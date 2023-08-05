package ru.yandex.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilations.dto.CompilationDto;
import ru.yandex.practicum.compilations.dto.NewCompilationDto;
import ru.yandex.practicum.compilations.dto.UpdateCompilationRequestDto;
import ru.yandex.practicum.compilations.service.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminCompilationService.createCompilation(newCompilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{compId}")
    public void deleteCompilation(@PathVariable int compId) {
        adminCompilationService.deleteCompilation(compId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/{compId}")
    public CompilationDto updateCompilation(@PathVariable int compId,
                                            @RequestBody @Valid UpdateCompilationRequestDto updateCompilationRequestDto) {
        return adminCompilationService.updateCompilation(compId, updateCompilationRequestDto);
    }
}
