package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.findUserById(userId);
        Set<Long> friendsIds = user.getFriends();

        return userStorage.findAll()
                .stream()
                .filter(u -> friendsIds.contains(u.getId()))
                .toList();
    }

    public Collection<User> getMutualFriends(Long userId, Long otherUserId) {
        User user1 = userStorage.findUserById(userId);
        User user2 = userStorage.findUserById(otherUserId);

        Set<Long> intersection = new HashSet<>(user1.getFriends());
        intersection.retainAll(user2.getFriends());

        return userStorage.findAll()
                .stream()
                .filter(u -> intersection.contains(u.getId()))
                .toList();
    }

    public void addFriend(Long userId, Long addedFriendId) {
        userStorage
                .findUserById(userId)
                .getFriends()
                .add(addedFriendId);

        userStorage
                .findUserById(addedFriendId)
                .getFriends()
                .add(userId);
    }

    public void deleteFriend(Long userId, Long deletedFriendId) {
        userStorage
                .findUserById(userId)
                .getFriends()
                .remove(deletedFriendId);
    }
}
