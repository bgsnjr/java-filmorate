package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains(Character.toString('@'))) {
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
        }
//        if (!newUser.getEmail().equals(oldUser.getEmail()) && doesEmailExist(newUser.getEmail())) {
//            throw new DuplicatedDataException("Этот имейл уже используется");
//        }
//        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
//            oldUser.setEmail(newUser.getEmail());
//        }
//        if (newUser.getUsername() != null && !newUser.getUsername().isBlank()) {
//            oldUser.setUsername(newUser.getUsername());
//        }
//        if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
//            oldUser.setPassword(newUser.getPassword());
//        }

        return oldUser;
    }

//    boolean doesEmailExist(String email) {
//        return users.values().stream().anyMatch(t -> t.getEmail().equalsIgnoreCase(email));
//    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
