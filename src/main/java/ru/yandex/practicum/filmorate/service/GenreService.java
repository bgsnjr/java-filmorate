package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponseDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;

    public List<GenreResponseDto> findAllGenres() {
        return genreRepository.findAllGenres()
                .stream()
                .map(GenreMapper::toDto)
                .toList();
    }

    public GenreResponseDto findGenreById(Integer id) {
        return GenreMapper.toDto(getGenreOrThrow(id));
    }

    private Genre getGenreOrThrow(Integer id) {
        return genreRepository.findGenreById(id)
                .orElseThrow(() ->
                        new NotFoundException("Genre with id " + id + " not found"));
    }
}
