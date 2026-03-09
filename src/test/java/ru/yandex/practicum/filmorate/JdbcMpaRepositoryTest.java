package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.mpa.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcMpaRepository.class, MpaRowMapper.class})
public class JdbcMpaRepositoryTest {
    private final MpaRepository mpaRepository;

    @Test
    public void testFindRatingById() {
        Integer ratingId = 3;

        Optional<MPA> mpaOptional = mpaRepository.findRatingById(ratingId);

        assertThat(mpaOptional).isPresent().hasValueSatisfying(f -> assertThat(f)
                .hasFieldOrPropertyWithValue("id", ratingId)
                .hasFieldOrPropertyWithValue("name", "PG-13")
        );
    }

    @Test
    public void testFindAllRatings() {
        List<MPA> ratings = mpaRepository.findAllRatings();

        assertAll(
                () -> assertEquals(5, ratings.size()),
                () -> assertThat(ratings.stream().map(MPA::getId).toList())
                        .containsAll(List.of(1, 2, 3, 4, 5)),
                () -> assertThat(ratings.stream().map(MPA::getName).toList())
                        .containsAll(List.of("G", "PG", "PG-13", "R", "NC-17"))
        );
    }
}
