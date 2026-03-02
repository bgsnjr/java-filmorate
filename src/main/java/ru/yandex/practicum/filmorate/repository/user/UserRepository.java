package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserById(Long id);

    List<User> findAllUsers();

    void addFriend(Long userId, Long friendId, FriendshipStatus status);

    void updateFriendshipStatus(Long userId, Long friendId, FriendshipStatus status);

    Optional<Integer> getFriendshipStatus(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> findFriends(Long userId);

    List<User> findMutualFriends(Long userId, Long otherUserId);

}
