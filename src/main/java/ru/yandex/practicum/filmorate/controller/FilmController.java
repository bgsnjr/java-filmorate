package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.IdGenerator.getNextId;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getNextId(films));
        films.put(film.getId(), film);
        log.trace("New film with id " + film.getId() + " created");

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            log.error("Id not found error");
            throw new NotFoundException("Film with id = " + newFilm.getId() + " not found");
        }
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.trace(
                "Film with id {} updated. New name: {}, new description: {}, new release date: {}, new duration: {}",
                oldFilm.getId(), oldFilm.getName(), oldFilm.getDescription(), oldFilm.getReleaseDate(), oldFilm.getDuration()
        );

        return oldFilm;
    }

}
