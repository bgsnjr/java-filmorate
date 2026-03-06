package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public FilmResponseDto createFilm(FilmCreateDto dto) {
        validateRating(dto.getMpa().getId());

        Set<Genre> genres = dto.getGenres();
        if (genres != null && !genres.isEmpty()) {
            validateGenres(genres);
        }

        Film film = filmRepository.createFilm(FilmMapper.toModel(dto));

        log.debug("Created film with id {}", film.getId());

        return FilmMapper.toDto(film);
    }

    public FilmResponseDto updateFilm(FilmUpdateDto dto) {
        validateRating(dto.getMpa().getId());

        Set<Genre> genres = dto.getGenres();
        if (genres != null && !genres.isEmpty()) {
            validateGenres(genres);
        }

        Film film = filmRepository.updateFilm(FilmMapper.toModel(dto));

        log.debug("Updated film with id {}", film.getId());

        return FilmMapper.toDto(film);
    }

    public FilmResponseDto findFilmById(Long id) {
        return FilmMapper.toDto(getFilmOrThrow(id));
    }

    public List<FilmResponseDto> findAllFilms() {
        return filmRepository.findAllFilms()
                .stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    public List<FilmResponseDto> findMostPopularFilms(Integer count) {
        return filmRepository.findMostPopularFilms(count)
                .stream()
                .map(FilmMapper::toDto)
                .toList();
    }

    public void addLike(Long filmId, Long userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);

        filmRepository.addLike(filmId, userId);

        log.debug("User with id {} added like to film with id {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        getFilmOrThrow(filmId);
        getUserOrThrow(userId);

        filmRepository.deleteLike(filmId, userId);

        log.debug("User with id {} deleted like from film with id {}", userId, filmId);
    }

    private Film getFilmOrThrow(Long id) {
        return filmRepository.findFilmById(id)
                .orElseThrow(() ->
                        new NotFoundException("Film with id " + id + " not found"));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + id + " not found"));
    }

    private void validateRating(Integer id) {
        mpaRepository.findRatingById(id)
                .orElseThrow(() ->
                        new NotFoundException("Rating with id " + id + " not found"));
    }

    private void validateGenres(Set<Genre> genres) {
        genres.stream()
                .map(Genre::getId)
                .forEach(id -> genreRepository.findGenreById(id)
                        .orElseThrow(() -> new NotFoundException("Genre with id " + id + " not found")));
    }
}