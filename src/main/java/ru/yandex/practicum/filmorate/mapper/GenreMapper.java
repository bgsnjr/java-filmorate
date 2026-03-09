package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.genre.GenreResponseDto;
import ru.yandex.practicum.filmorate.model.Genre;

public final class GenreMapper {
    public static GenreResponseDto toDto(Genre genre) {
        return GenreResponseDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
