package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.user.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

public final class UserMapper {
    public static User toModel(UserCreateDto dto) {
        String name = dto.getName();

        if (name == null || name.isBlank()) {
            name = dto.getLogin();
        }

        return User.builder()
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(name)
                .birthday(dto.getBirthday())
                .build();
    }

    public static User toModel(UserUpdateDto dto) {
        String name = dto.getName();

        if (name == null || name.isBlank()) {
            name = dto.getLogin();
        }

        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(name)
                .birthday(dto.getBirthday())
                .build();
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
    }
}
