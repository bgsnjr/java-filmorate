package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.IdGenerator.getNextId;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId(users));
        users.put(user.getId(), user);
        log.trace("New user with id " + user.getId() + " created");

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            log.error("Id not found error");
            throw new NotFoundException("User with id = " + newUser.getId() + " not found");
        }
        oldUser.setEmail(newUser.getEmail());
        log.trace("Set new email: {}", newUser.getName());
        oldUser.setLogin(newUser.getLogin());
        log.trace("Set new login: {}", newUser.getLogin());
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }
        log.trace("Set new name: {}", newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.trace("Set new birthday date: {}", newUser.getBirthday());
        log.trace("User with id " + oldUser.getId() + " updated");

        return oldUser;
    }

}
