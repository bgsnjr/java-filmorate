package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void addFriend(Long userId, Long friendId, FriendshipStatus status);

    void updateFriendshipStatus(Long userId, Long friendId, FriendshipStatus status);

    boolean existsFriendship(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> findFriends(Long userId);

    List<User> findMutualFriends(Long userId, Long otherUserId);
}
