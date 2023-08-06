package ru.yandex.practicum.compilations.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {

    private List<Integer> events;

    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
