package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public Collection<User> getFriends(Long userId) {
        Objects.requireNonNull(userId, "userId must not be null");

        User user = userStorage.findUserById(userId);
        if (user == null) {
            log.error("Id not found error");
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Set<Long> friendsIds = user.getFriends();
        if (friendsIds.isEmpty()) {
            return List.of();
        }

        return userStorage.findUserIds()
                .stream()
                .filter(friendsIds::contains)
                .map(userStorage::findUserById)
                .filter(Objects::nonNull)
                .toList();
    }

    public Collection<User> getMutualFriends(Long userId, Long otherUserId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(otherUserId, "otherUserId must not be null");

        User user1 = getUserById(userId);
        User user2 = getUserById(otherUserId);

        Set<Long> friendsList1 = Optional.ofNullable(user1.getFriends()).orElse(Set.of());
        Set<Long> friendsList2 = Optional.ofNullable(user2.getFriends()).orElse(Set.of());
        Set<Long> intersection = new HashSet<>(friendsList1);
        intersection.retainAll(friendsList2);

        return userStorage.findUserIds()
                .stream()
                .filter(intersection::contains)
                .map(userStorage::findUserById)
                .filter(Objects::nonNull)
                .toList();
    }

    public void addFriend(Long userId, Long addedFriendId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(addedFriendId, "addedFriendId must not be null");

        if (userId.equals(addedFriendId)) {
            throw new ValidationException("User cannot add themselves as a friend");
        }

        User user = getUserById(userId);
        User addedFriend = getUserById(addedFriendId);

        user.getFriends().add(addedFriendId);
        addedFriend.getFriends().add(userId);
        log.trace("User with id " + userId + " and user with id " + addedFriendId + " became friends");

    }

    public void deleteFriend(Long userId, Long deletedFriendId) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(deletedFriendId, "deletedFriendId must not be null");

        User user = getUserById(userId);
        User deletedFriend = getUserById(deletedFriendId);

        boolean removedFromUser = user.getFriends().remove(deletedFriendId);
        boolean removedFromDeletedFriend = deletedFriend.getFriends().remove(userId);

        if (!removedFromUser || !removedFromDeletedFriend) {
            log.warn("Id not in friends list");
        } else {
            log.trace("User with id " + userId + " removed user with id " + deletedFriendId + " from friends");
        }
    }

    private User getUserById(Long userId) {
        User user = userStorage.findUserById(userId);
        if (user == null) {
            log.error("Id not found error");
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return user;
    }
}
