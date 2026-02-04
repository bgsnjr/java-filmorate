package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.IdGenerator.getNextId;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId(users));
        users.put(user.getId(), user);
        log.trace("New user with id " + user.getId() + " created");

        return user;
    }

    @Override
    public User update(User newUser) {
        User oldUser = findUserById(newUser.getId());
        if (oldUser == null) {
            log.error("Id not found error");
            throw new NotFoundException("User with id = " + newUser.getId() + " not found");
        }
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }
        oldUser.setBirthday(newUser.getBirthday());
        log.trace(
                "User with id {} updated. New email: {}, new login: {}, new name: {}, new birthday date: {}",
                oldUser.getId(), oldUser.getEmail(), oldUser.getLogin(), oldUser.getName(), oldUser.getBirthday()
        );

        return oldUser;
    }

    @Override
    public User findUserById(Long id) {
        return users.get(id);
    }

    @Override
    public Collection<Long> findUserIds() {
        return users.keySet();
    }


}
