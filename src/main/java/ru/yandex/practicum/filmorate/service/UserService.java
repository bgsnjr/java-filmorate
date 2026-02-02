package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getFriends(Long userId) {
        User user = userStorage.findUserById(userId);
        Set<Long> friendsIds = user.getFriends();

        return userStorage.findAll()
                .stream()
                .filter(u -> friendsIds.contains(u.getId()))
                .toList();
    }

    public List<User> getMutualFriends(Long userId1, Long userId2) {
        User user1 = userStorage.findUserById(userId1);
        User user2 = userStorage.findUserById(userId2);

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
