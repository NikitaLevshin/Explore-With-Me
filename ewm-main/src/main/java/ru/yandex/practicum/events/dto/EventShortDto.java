package ru.yandex.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.categories.dto.CategoryDto;
import ru.yandex.practicum.user.dto.UserShortDto;

@Data
@Builder
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String eventDate;
    private int id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private int views;
}
