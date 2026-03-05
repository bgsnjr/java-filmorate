package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final JdbcTemplate jdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        String query = """
                SELECT *
                FROM genres
                WHERE id = ?
                """;

        List<Genre> genres = jdbc.query(query, genreRowMapper, id);

        return genres.stream().findFirst();
    }

    @Override
    public Set<Genre> findGenresByFilmId(Long id) {
        String query = """
                SELECT g.*
                FROM genres g
                JOIN films_genres fg ON fg.genre_id = g.id
                WHERE fg.film_id = ?
                """;

        return new HashSet<>(jdbc.query(query, genreRowMapper, id));
    }

    @Override
    public void addGenresToFilm(Long filmId, Set<Genre> genres) {
        String query = """
                INSERT INTO films_genres (film_id, genre_id)
                VALUES (?, ?)
                """;

        List<Object[]> batch = genres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        jdbc.batchUpdate(query, batch);
    }

    @Override
    public List<Genre> findAllGenres() {
        String query = """
                SELECT *
                FROM genres
                ORDER BY id
                """;

        return jdbc.query(query, genreRowMapper);
    }
}
