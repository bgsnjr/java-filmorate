package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    Optional<Genre> findGenreById(Integer id);

    Set<Genre> findGenresByFilmId(Long id);

    void insertGenresForFilm(Long id, Set<Genre> genres);

    List<Genre> findAllGenres();
}
