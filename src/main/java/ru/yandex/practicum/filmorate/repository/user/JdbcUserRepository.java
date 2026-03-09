package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbc;
    private final UserRowMapper userRowMapper;

    @Override
    public User createUser(User user) {
        String query = """
                INSERT INTO users (email, login, name, birthday)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    query,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());

            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        String query = """
                UPDATE users
                SET email = ?, login = ?, name = ?, birthday = ?
                WHERE id = ?
                """;

        int updated = jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            ps.setLong(5, user.getId());

            return ps;
        });

        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        String query = """
                SELECT id, email, login, name, birthday
                FROM users
                WHERE id = ?
                """;

        List<User> users = jdbc.query(query, userRowMapper, id);

        return users.stream().findFirst();
    }

    @Override
    public List<User> findAllUsers() {
        String query = """
                SELECT id, email, login, name, birthday
                FROM users
                ORDER BY id
                """;

        return jdbc.query(query, userRowMapper);
    }

    @Override
    public void addFriend(Long userId, Long friendId, FriendshipStatus status) {
        String query = """
                INSERT INTO friendships (user_id, friend_id, status_id)
                VALUES (?, ?, ?)
                """;

        jdbc.update(query, userId, friendId, status.getId());
    }

    @Override
    public void updateFriendshipStatus(Long userId, Long friendId, FriendshipStatus status) {
        String query = """
                UPDATE friendships
                SET status_id = ?
                WHERE user_id = ? AND friend_id = ?
                """;

        jdbc.update(query, status.getId(), userId, friendId);
    }

    @Override
    public Optional<Integer> getFriendshipStatus(Long userId, Long friendId) {
        String query = """
                SELECT status_id
                FROM friendships
                WHERE user_id = ? AND friend_id = ?
                """;

        List<Integer> result = jdbc.query(query, (rs, rowNum) -> rs.getInt("status_id"), userId, friendId);

        return result.stream().findFirst();
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
                SELECT u.id,
                       u.email,
                       u.login,
                       u.name,
                       u.birthday
                FROM friendships f
                JOIN users u ON u.id = f.friend_id
                WHERE f.user_id = ?
                """;

        return jdbc.query(query, userRowMapper, userId);
    }

    @Override
    public List<User> findMutualFriends(Long userId, Long otherUserId) {
        String query = """
                SELECT u.id,
                       u.email,
                       u.login,
                       u.name,
                       u.birthday
                FROM friendships f1
                JOIN friendships f2 ON f1.friend_id = f2.friend_id
                JOIN users u ON u.id = f1.friend_id
                WHERE f1.user_id = ? AND f2.user_id = ?
                """;

        return jdbc.query(query, userRowMapper, userId, otherUserId);
    }
}
