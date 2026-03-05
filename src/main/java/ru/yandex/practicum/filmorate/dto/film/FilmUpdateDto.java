package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.validation.annotation.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class FilmUpdateDto {
    @NotNull
    private Long id;

    @NotBlank(message = "{film.name.notBlank}")
    private String name;

    @Size(max = 200, message = "{film.description.maxLength}")
    private String description;

    @MinDate(value = "1895-12-28")
    private LocalDate releaseDate;

    @Positive(message = "{film.duration.positive}")
    private Integer duration;

    @NotNull
    private MPA mpa;

    @Builder.Default
    private Set<Genre> genres = new HashSet<>();
}
