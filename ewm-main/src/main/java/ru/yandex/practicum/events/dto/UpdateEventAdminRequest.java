package ru.yandex.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.events.model.StateActionAdmin;
import ru.yandex.practicum.location.model.Location;

import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateActionAdmin stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
