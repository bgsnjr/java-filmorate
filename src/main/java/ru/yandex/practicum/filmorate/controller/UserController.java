package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Email validation error");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains(Character.toString('@'))) {
            log.error("Email validation error");
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Login validation error");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Login validation error");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday date validation error");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.trace("New user with id " + user.getId() + " created");

        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.error("Id validation error");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
                log.error("Email validation error");
                throw new ValidationException("Электронная почта не может быть пустой");
            }
            if (!newUser.getEmail().contains(Character.toString('@'))) {
                log.error("Email validation error");
                throw new ValidationException("Электронная почта должна содержать символ @");
            }
            oldUser.setEmail(newUser.getEmail());
            if (newUser.getLogin() == null || newUser.getLogin().isBlank()) {
                log.error("Login validation error");
                throw new ValidationException("Логин не может быть пустым");
            }
            if (newUser.getLogin().contains(" ")) {
                log.error("Login validation error");
                throw new ValidationException("Логин не может содержать пробелы");
            }
            oldUser.setLogin(newUser.getLogin());
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
            }
            oldUser.setName(newUser.getName());
            if (newUser.getBirthday() != null) {
                if (newUser.getBirthday().isAfter(LocalDate.now())) {
                    log.error("Birthday date validation error");
                    throw new ValidationException("Дата рождения не может быть в будущем");
                }
                oldUser.setBirthday(newUser.getBirthday());
            }
            log.trace("User with id " + oldUser.getId() + " updated");

            return oldUser;
        }

        log.error("Id not found error");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
