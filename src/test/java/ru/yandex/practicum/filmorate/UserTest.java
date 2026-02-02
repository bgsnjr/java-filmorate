package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassWhenValidFields() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .login("john-doe")
                .name("John Doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenEmailNull() {
        User film = User.builder()
                .login("john-doe")
                .name("John Doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenEmailBlank() {
        User film = User.builder()
                .email("")
                .login("john-doe")
                .name("John Doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenEmailInvalid() {
        User film = User.builder()
                .email("johndoe@")
                .login("john-doe")
                .name("John Doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenLoginNull() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .name("John Doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenLoginBlank() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .login("")
                .name("John Doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenLoginContainsSpace() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .login("john doe")
                .name("John Doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenNameNull() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .login("john-doe")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassWhenNameEmpty() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .login("john-doe")
                .name("")
                .birthday(LocalDate.of(1986, 12, 1))
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassWhenBirthdayToday() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .login("john-doe")
                .name("John Doe")
                .birthday(LocalDate.now())
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenBirthdayTomorrow() {
        User film = User.builder()
                .email("john-doe@mail.ru")
                .login("john-doe")
                .name("John Doe")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

}
