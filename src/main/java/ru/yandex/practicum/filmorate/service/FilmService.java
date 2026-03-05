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
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
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

//
//    public void addLike(Long filmId, Long userId) {
//        Objects.requireNonNull(filmId, "filmId must not be null");
//        Objects.requireNonNull(userId, "userId must not be null");
//
//        Film film = getFilmById(filmId);
//        getUserById(userId);
//
//        film.getUserLikes().add(userId);
//        log.trace("Film with id " + filmId + " got like from user with id " + userId);
//    }
//
//    public void deleteLike(Long filmId, Long userId) {
//        Objects.requireNonNull(filmId, "filmId must not be null");
//        Objects.requireNonNull(userId, "userId must not be null");
//
//        Film film = getFilmById(filmId);
//        getUserById(userId);
//
//        boolean removed = film.getUserLikes().remove(userId);
//        if (!removed) {
//            log.error("Id not found error");
//            throw new NotFoundException("Like from user with id " + userId + " not found");
//        }
//        log.trace("User with id " + userId + " removed like from film with id " + filmId);
//    }
//
//    public Collection<Film> getTop10Films(Integer count) {
//        if (count == null || count < 1) {
//            log.error("Path variable validation error");
//            throw new ValidationException("Count value must be positive");
//        }
//
//        Collection<Film> films = filmStorage.findAll();
//        if (films == null || films.isEmpty()) {
//            return List.of();
//        }
//
//        return films
//                .stream()
//                .filter(Objects::nonNull)
//                .sorted(Comparator.comparing(Film::getLikesNumber).reversed())
//                .limit(count)
//                .toList();
//    }
//
//    private Film getFilmById(Long filmId) {
//        Film film = filmStorage.findFilmById(filmId);
//        if (film == null) {
//            log.error("Id not found error");
//            throw new NotFoundException("Film with id " + filmId + " not found");
//        }
//        return film;
//    }
//
//    private User getUserById(Long userId) {
//        User user = userStorage.findUserById(userId);
//        if (user == null) {
//            log.error("Id not found error");
//            throw new NotFoundException("User with id " + userId + " not found");
//        }
//        return user;
//    }

    private Film getFilmOrThrow(Long id) {
        return filmRepository.findFilmById(id)
                .orElseThrow(() ->
                        new NotFoundException("Film with id " + id + " not found"));
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