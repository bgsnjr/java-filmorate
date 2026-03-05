package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponseDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<GenreResponseDto> findAllGenres() {
        return genreService.findAllGenres();
    }

    @GetMapping("/{id}")
    public GenreResponseDto findGenreById(@PathVariable @Positive Integer id) {
        return genreService.findGenreById(id);
    }
}
