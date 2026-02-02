package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

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
        return findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return update(newUser);
    }

}
