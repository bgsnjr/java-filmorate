package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> findFilmById(Long id);

    List<Film> findAllFilms();

    List<Film> findMostPopularFilms(Integer count);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
