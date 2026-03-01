package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        Date date = rs.getDate("birthday");

        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(date != null ? date.toLocalDate() : null)
                .build();
    };

    @Override
    public User create(User user) {
        String sql = """
                INSERT INTO users (email, login, name, birthday)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, user.getBirthday() != null
                    ? Date.valueOf(user.getBirthday())
                    : null);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated user id");
        }

        user.setId(key.longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = """
                UPDATE users
                SET email = ?, login = ?, name = ?, birthday = ?
                WHERE id = ?
                """;

        int updated = jdbc.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday() != null
                        ? Date.valueOf(user.getBirthday())
                        : null,
                user.getId()
        );

        if (updated == 0) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        String query = """
                SELECT *
                FROM users
                WHERE id = ?
                """;

        List<User> users = jdbc.query(query, userRowMapper, id);

        return users.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        String query = """
                SELECT *
                FROM users
                """;

        return jdbc.query(query, userRowMapper);
    }

    @Override
    public void addFriend(Long userId, Long friendId, FriendshipStatus status) {
        String sql = """
                INSERT INTO friendships (user_id, friend_id, status_id)
                VALUES (?, ?, ?)
                """;

        try {
            jdbc.update(sql, userId, friendId, status.getId());
        } catch (DuplicateKeyException e) {
        }
    }

    @Override
    public void updateFriendshipStatus(Long userId,
                                       Long friendId,
                                       FriendshipStatus status) {
        String sql = """
                UPDATE friendships
                SET status_id = ?
                WHERE user_id = ?
                AND friend_id = ?
                """;

        int updated = jdbc.update(sql, status.getId(), userId, friendId);

        if (updated == 0) {
            throw new NotFoundException("Friendship not found");
        }
    }

    @Override
    public boolean existsFriendship(Long userId, Long friendId) {
        String sql = """
                SELECT 1
                FROM friendships
                WHERE user_id = ?
                AND friend_id = ?
                LIMIT 1
                """;

        List<Integer> result = jdbc.query(sql, (rs, rowNum) -> rs.getInt(1), userId, friendId);

        return !result.isEmpty();
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String query = """
                DELETE FROM friendships
                WHERE user_id = ? AND friend_id = ?
                """;

        jdbc.update(query, userId, friendId);
    }

    @Override
    public List<User> findFriends(Long userId) {
        String query = """
                SELECT u.*
                FROM friendships f
                JOIN users u ON u.id = f.friend_id
                WHERE f.user_id = ?
                """;

        return jdbc.query(query, userRowMapper, userId);
    }

    @Override
    public List<User> findMutualFriends(Long userId, Long otherUserId) {
        String query = """
                SELECT u.*
                FROM friendships f1
                JOIN friendships f2 ON f1.friend_id = f2.friend_id
                JOIN users u ON u.id = f1.friend_id
                WHERE f1.user_id = ?
                AND f2.user_id = ?
                """;

        return jdbc.query(query, userRowMapper, userId, otherUserId);
    }
}
