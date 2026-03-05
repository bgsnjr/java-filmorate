package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public final class FilmMapper {
    public static Film toModel(FilmCreateDto dto) {
        return Film.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpa(dto.getMpa())
                .genres(dto.getGenres())
                .build();
    }

    public static Film toModel(FilmUpdateDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpa(dto.getMpa())
                .genres(dto.getGenres())
                .build();
    }

    public static FilmResponseDto toDto(Film film) {
        return FilmResponseDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .genres(
                        film.getGenres().stream()
                                .sorted(Comparator.comparing(Genre::getId))
                                .map(GenreMapper::toDto)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                ).build();
    }
}
