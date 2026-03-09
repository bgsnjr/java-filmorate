package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.repository.genre.JdbcGenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcGenreRepository.class, GenreRowMapper.class})
public class JdbcGenreRepositoryTest {
    private final GenreRepository genreRepository;

    @Test
    public void testFindGenreById() {
        Integer genreId = 3;

        Optional<Genre> genreOptional = genreRepository.findGenreById(genreId);

        assertThat(genreOptional).isPresent().hasValueSatisfying(f -> assertThat(f)
                .hasFieldOrPropertyWithValue("id", genreId)
                .hasFieldOrPropertyWithValue("name", "Мультфильм")
        );
    }

    @Test
    public void testFindAllGenres() {
        List<Genre> genres = genreRepository.findAllGenres();

        assertAll(
                () -> assertEquals(6, genres.size()),
                () -> assertThat(genres.stream().map(Genre::getId).toList())
                        .containsAll(List.of(1, 2, 3, 4, 5, 6)),
                () -> assertThat(genres.stream().map(Genre::getName).toList())
                        .containsAll(List.of("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик"))
        );
    }
}
