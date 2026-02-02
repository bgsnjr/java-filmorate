package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    @GetMapping
    public Collection<Film> findAll() {
        return findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return update(newFilm);
    }

}
