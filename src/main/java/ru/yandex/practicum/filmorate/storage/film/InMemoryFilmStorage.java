package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.IdGenerator.getNextId;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId(films));
        films.put(film.getId(), film);
        log.trace("New film with id " + film.getId() + " created");

        return film;
    }

    @Override
    public Film update(Film newFilm) {
        Film oldFilm = findFilmById(newFilm.getId());
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

    @Override
    public Film findFilmById(Long id) {
        return films.get(id);
    }

}
