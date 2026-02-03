package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Long filmId, Long userId) {
        filmStorage
                .findFilmById(filmId)
                .getUserLikes()
                .add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage
                .findFilmById(filmId)
                .getUserLikes()
                .remove(userId);
    }

    public Collection<Film> getTop10Films(Integer count) {
        return filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(count)
                .toList();
    }
}
