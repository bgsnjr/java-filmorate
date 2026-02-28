package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.annotation.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    Long id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    String description;
    @MinDate(value = "1895-12-28")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Integer duration;
    Set<Long> userLikes;
    Set<Genre> genre;
    Rating rating;

    public Set<Long> getUserLikes() {
        if (userLikes == null) {
            userLikes = new HashSet<>();
        }
        return userLikes;
    }

    public int getLikesNumber() {
        return userLikes.size();
    }
}
