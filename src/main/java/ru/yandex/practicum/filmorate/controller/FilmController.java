package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.film.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.film.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmResponseDto createFilm(@Valid @RequestBody FilmCreateDto dto) {
        return filmService.createFilm(dto);
    }

    @PutMapping
    public FilmResponseDto updateFilm(@Valid @RequestBody FilmUpdateDto dto) {
        return filmService.updateFilm(dto);
    }

    @GetMapping("/{id}")
    public FilmResponseDto findFilmById(@PathVariable @Positive Long id) {
        return filmService.findFilmById(id);
    }

    @GetMapping
    public List<FilmResponseDto> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/popular")
    public List<FilmResponseDto> findMostPopularFilms(@RequestParam(defaultValue = "10") @PositiveOrZero Integer count) {
        return filmService.findMostPopularFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable @Positive Long filmId, @PathVariable @Positive Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable @Positive Long filmId, @PathVariable @Positive Long userId) {
        filmService.deleteLike(filmId, userId);
    }

}
