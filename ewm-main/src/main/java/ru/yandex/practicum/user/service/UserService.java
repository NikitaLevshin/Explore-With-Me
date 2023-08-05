package ru.yandex.practicum.user.service;

import ru.yandex.practicum.user.dto.NewUserRequestDto;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.model.User;

import java.util.List;

public interface UserService {

    UserDto create(NewUserRequestDto newUserRequestDto);

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    void deleteUser(int id);

    User getUserById(int id);
}
