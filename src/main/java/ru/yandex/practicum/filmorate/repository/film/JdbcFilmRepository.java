package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.genre.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.JdbcMpaRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final JdbcTemplate jdbc;
    private final FilmRowMapper filmRowMapper;
    private final JdbcGenreRepository jdbcGenreRepository;
    private final JdbcMpaRepository jdbcMpaRepository;

    @Override
    public Film createFilm(Film film) {
        String query = """
                INSERT INTO films (name, description, release_date, duration, rating_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    query,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());

            return ps;
        }, keyHolder);

        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(generatedId);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            jdbcGenreRepository.insertGenresForFilm(generatedId, film.getGenres());
        }

        setGenresForFilm(film);
        setMpaForFilm(film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String query = """
                UPDATE films
                SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ?
                WHERE id = ?
                """;

        int updated = jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());
            ps.setLong(6, film.getId());

            return ps;
        });

        if (film.getGenres() != null) {
            jdbc.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());

            if (!film.getGenres().isEmpty()) {
                jdbcGenreRepository.insertGenresForFilm(film.getId(), film.getGenres());
            }
        }

        setGenresForFilm(film);
        setMpaForFilm(film);

        return film;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String query = """
                SELECT f.id,
                    f.name,
                    f.description,
                    f.release_date,
                    f.duration,
                    f.rating_id,
                    r.name rating_name
                FROM films f
                JOIN ratings r ON r.id = f.rating_id
                WHERE f.id = ?
                """;

        Optional<Film> film = jdbc.query(query, filmRowMapper, id)
                .stream()
                .findFirst();

        film.ifPresent(this::setGenresForFilm);

        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        String query = """
                SELECT f.id,
                    f.name,
                    f.description,
                    f.release_date,
                    f.duration,
                    f.rating_id,
                    r.name rating_name
                FROM films f
                JOIN ratings r ON r.id = f.rating_id
                """;

        List<Film> films = jdbc.query(query, filmRowMapper);

        setGenresForFilms(films);

        return films;
    }

    @Override
    public List<Film> findMostPopularFilms(Integer count) {
        String query = """
                SELECT f.id,
                    f.name,
                    f.description,
                    f.release_date,
                    f.duration,
                    f.rating_id,
                    r.name rating_name,
                    COUNT(fl.film_id)
                FROM films f
                JOIN ratings r ON r.id = f.rating_id
                JOIN films_likes fl ON fl.film_id = f.id
                GROUP BY f.id
                ORDER BY COUNT(fl.film_id) DESC
                LIMIT ?
                """;

        List<Film> films = jdbc.query(query, filmRowMapper, count);

        setGenresForFilms(films);

        return films;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String query = """
                INSERT INTO films_likes (film_id, user_id)
                VALUES (?, ?)
                """;

        jdbc.update(query, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String query = """
                DELETE FROM films_likes
                WHERE film_id = ? AND user_id = ?
                """;

        jdbc.update(query, filmId, userId);
    }

    private void setGenresForFilm(Film film) {
        film.setGenres(jdbcGenreRepository.findGenresByFilmId(film.getId()));
    }

    private void setGenresForFilms(List<Film> films) {
        for (Film film : films) {
            setGenresForFilm(film);
        }
    }

    private void setMpaForFilm(Film film) {
        Integer ratingId = film.getMpa().getId();
        film.setMpa(
                jdbcMpaRepository.findRatingById(ratingId)
                        .orElseThrow(() -> new RuntimeException("Rating with id " + ratingId + " not found"))
        );
    }
}
