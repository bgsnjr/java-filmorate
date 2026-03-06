package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final JdbcTemplate jdbc;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Optional<MPA> findRatingById(Integer id) {
        String query = """
                SELECT id, name
                FROM ratings
                WHERE id = ?
                """;
        List<MPA> ratings = jdbc.query(query, mpaRowMapper, id);

        return ratings.stream().findFirst();
    }

    @Override
    public List<MPA> findAllRatings() {
        String query = """
                SELECT id, name
                FROM ratings
                ORDER BY id
                """;

        return jdbc.query(query, mpaRowMapper);
    }
}
