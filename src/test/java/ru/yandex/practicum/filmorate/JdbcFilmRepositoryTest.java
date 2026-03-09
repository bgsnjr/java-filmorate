package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.film.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.repository.genre.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRowMapper;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRowMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        JdbcFilmRepository.class,
        JdbcGenreRepository.class,
        JdbcMpaRepository.class,
        JdbcUserRepository.class,
        FilmRowMapper.class,
        GenreRowMapper.class,
        MpaRowMapper.class,
        UserRowMapper.class
})
public class JdbcFilmRepositoryTest {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Test
    public void testCreateFilm() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));

        Film film = Film.builder()
                .name("Film Name")
                .description("Film Description")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(120)
                .mpa(new MPA(1, "G"))
                .genres(genres)
                .build();

        Film created = filmRepository.createFilm(film);

        assertAll(
                () -> assertNotNull(created.getId()),
                () -> assertEquals(film.getName(), created.getName()),
                () -> assertEquals(film.getDescription(), created.getDescription()),
                () -> assertEquals(film.getReleaseDate(), created.getReleaseDate()),
                () -> assertEquals(film.getMpa(), created.getMpa()),
                () -> assertEquals(film.getGenres(), created.getGenres())
        );
    }

    @Test
    public void testUpdateFilm() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));

        Set<Genre> newGenres = new HashSet<>();
        newGenres.add(new Genre(1, "Комедия"));
        newGenres.add(new Genre(3, "Мультфильм"));

        Film film = Film.builder()
                .name("Film Name")
                .description("Film Description")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(120)
                .mpa(new MPA(1, "G"))
                .genres(genres)
                .build();

        Long filmId = filmRepository.createFilm(film).getId();

        Film filmWithNewData = Film.builder()
                .id(filmId)
                .name("Updated Name")
                .description("Updated Description")
                .releaseDate(LocalDate.of(2000, 10, 20))
                .duration(110)
                .mpa(new MPA(2, "PG"))
                .genres(newGenres)
                .build();

        Film updated = filmRepository.createFilm(filmWithNewData);

        assertAll(
                () -> assertEquals(filmWithNewData.getName(), updated.getName()),
                () -> assertEquals(filmWithNewData.getDescription(), updated.getDescription()),
                () -> assertEquals(filmWithNewData.getReleaseDate(), updated.getReleaseDate()),
                () -> assertEquals(filmWithNewData.getMpa(), updated.getMpa()),
                () -> assertEquals(filmWithNewData.getGenres(), updated.getGenres())
        );
    }

    @Test
    public void testFindFilmById() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));

        Film film = Film.builder()
                .name("Film Name")
                .description("Film Description")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(120)
                .mpa(new MPA(1, "G"))
                .genres(genres)
                .build();

        Long filmId = filmRepository.createFilm(film).getId();

        Optional<Film> filmOptional = filmRepository.findFilmById(filmId);

        assertThat(filmOptional).isPresent().hasValueSatisfying(f ->
                assertThat(f).hasFieldOrPropertyWithValue("id", filmId)
        );
    }

    @Test
    public void testFindAllFilms() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));

        Film film1 = Film.builder()
                .name("First Film Name")
                .description("First Film Description")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(120)
                .mpa(new MPA(1, "G"))
                .genres(genres)
                .build();

        Film film2 = Film.builder()
                .name("Second Film Name")
                .description("Second Film Description")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(120)
                .mpa(new MPA(1, "G"))
                .genres(genres)
                .build();

        Long firstFilmId = filmRepository.createFilm(film1).getId();
        Long secondFilmId = filmRepository.createFilm(film2).getId();

        List<Film> films = filmRepository.findAllFilms();

        assertAll(
                () -> assertEquals(2, films.size()),
                () -> assertThat(films.stream().map(Film::getId).toList())
                        .containsAll(List.of(firstFilmId, secondFilmId))
        );
    }

    @Test
    public void testFilmLikes() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));

        Film film1 = Film.builder()
                .name("First Film Name")
                .description("First Film Description")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(120)
                .mpa(new MPA(1, "G"))
                .genres(genres)
                .build();

        Film film2 = Film.builder()
                .name("Second Film Name")
                .description("Second Film Description")
                .releaseDate(LocalDate.of(1990, 10, 20))
                .duration(120)
                .mpa(new MPA(1, "G"))
                .genres(genres)
                .build();

        User user1 = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        User user2 = User.builder()
                .email("alice@mail.com")
                .login("adoe")
                .name("Alice Doe")
                .birthday(LocalDate.of(1995, 5, 15)).build();

        Long firstFilmId = filmRepository.createFilm(film1).getId();
        Long secondFilmId = filmRepository.createFilm(film2).getId();
        Long firstUserId = userRepository.createUser(user1).getId();
        Long secondUserId = userRepository.createUser(user2).getId();

        filmRepository.addLike(firstFilmId, firstUserId);
        filmRepository.addLike(secondFilmId, firstUserId);
        filmRepository.addLike(secondFilmId, secondUserId);

        List<Film> topFilms = filmRepository.findMostPopularFilms(10);

        assertAll(
                () -> assertEquals(2, topFilms.size()),
                () -> assertThat(topFilms.stream().map(Film::getId).toList())
                        .containsExactlyElementsOf(List.of(secondFilmId, firstFilmId))
        );

        filmRepository.deleteLike(secondFilmId, secondUserId);
        filmRepository.addLike(firstFilmId, secondUserId);

        List<Film> updatedTopFilms = filmRepository.findMostPopularFilms(10);

        assertAll(
                () -> assertEquals(2, updatedTopFilms.size()),
                () -> assertThat(updatedTopFilms.stream().map(Film::getId).toList())
                        .containsExactlyElementsOf(List.of(firstFilmId, secondFilmId))
        );
    }
}
