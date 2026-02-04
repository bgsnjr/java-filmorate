package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Objects.requireNonNull(filmId, "filmId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");

        Film film = filmStorage.findFilmById(filmId);
        if (film == null) {
            log.error("Id not found error");
            throw new NotFoundException("Film with id " + filmId + " not found");
        }

        User user = userStorage.findUserById(userId);
        if (user == null) {
            log.error("Id not found error");
            throw new NotFoundException("User with id " + userId + " not found");
        }

        film.getUserLikes().add(userId);
        log.trace("Film with id " + filmId + " got like from user with id " + userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Objects.requireNonNull(filmId, "filmId must not be null");
        Objects.requireNonNull(userId, "userId must not be null");

        Film film = filmStorage.findFilmById(filmId);
        if (film == null) {
            log.error("Id not found error");
            throw new NotFoundException("Film with id " + filmId + " not found");
        }

        User user = userStorage.findUserById(userId);
        if (user == null) {
            log.error("Id not found error");
            throw new NotFoundException("User with id " + userId + " not found");
        }

        boolean removed = film.getUserLikes().remove(userId);
        if (!removed) {
            log.error("Id not found error");
            throw new NotFoundException("Like from user with id " + userId + " not found");
        }
        log.trace("User with id " + userId + " removed like from film with id " + filmId);
    }

    public Collection<Film> getTop10Films(Integer count) {
        if (count == null || count < 1) {
            log.error("Path variable validation error");
            throw new ValidationException("Count value must be positive");
        }

        Collection<Film> films = filmStorage.findAll();
        if (films == null || films.isEmpty()) {
            return List.of();
        }

        return films
                .stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Film::getLikesNumber).reversed())
                .limit(count)
                .toList();
    }

}
