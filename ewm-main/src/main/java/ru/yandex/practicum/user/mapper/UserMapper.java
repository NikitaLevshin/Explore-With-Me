package ru.yandex.practicum.user.mapper;

import ru.yandex.practicum.user.dto.NewUserRequestDto;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserShortDto;
import ru.yandex.practicum.user.model.User;

public class UserMapper {

    public static User fromUserDto(NewUserRequestDto newUserRequestDto) {
        return User.builder()
                .email(newUserRequestDto.getEmail())
                .name(newUserRequestDto.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
