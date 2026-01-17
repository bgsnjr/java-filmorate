package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    Long id;
    @NotNull(message = "Электронная почта не может быть null")
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна иметь корректный формат")
    String email;
    @NotNull(message = "Логин не может быть null")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    String login;
    String name;
    @PastOrPresent(message = "Дата рождения не может быть позже сегодняшнего дня")
    LocalDate birthday;
}
