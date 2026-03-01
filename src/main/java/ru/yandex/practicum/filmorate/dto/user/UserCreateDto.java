package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateDto {
    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.valid}")
    private String email;

    @NotBlank(message = "{user.login.notBlank}")
    @Pattern(regexp = "^\\S+$", message = "{user.login.noSpaces}")
    private String login;

    private String name;

    @PastOrPresent(message = "{user.birthday.valid}")
    private LocalDate birthday;
}
