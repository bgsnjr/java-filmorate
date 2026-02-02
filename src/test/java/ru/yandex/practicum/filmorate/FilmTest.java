package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassWhenValidFields() {
        Film film = Film.builder()
                .name("Кин-дза-дза!")
                .description("Прораб Владимир Николаевич Машков и не подозревал, что обычный путь до универсама " +
                        "за хлебом и макаронами обернется межгалактическим путешествием."
                )
                .releaseDate(LocalDate.of(1986, 12, 1))
                .duration(135)
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenNameNull() {
        Film film = Film.builder()
                .description("Прораб Владимир Николаевич Машков и не подозревал, что обычный путь до универсама " +
                        "за хлебом и макаронами обернется межгалактическим путешествием."
                )
                .releaseDate(LocalDate.of(1986, 12, 1))
                .duration(135)
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenNameBlank() {
        Film film = Film.builder()
                .name("")
                .description("Прораб Владимир Николаевич Машков и не подозревал, что обычный путь до универсама " +
                        "за хлебом и макаронами обернется межгалактическим путешествием."
                )
                .releaseDate(LocalDate.of(1986, 12, 1))
                .duration(135)
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenDescriptionSize200() {
        Film film = Film.builder()
                .name("Кин-дза-дза!")
                .description("Прораб Владимир Николаевич Машков и не подозревал, что обычный путь до универсама " +
                        "за хлебом и макаронами обернется межгалактическим путешествием. А все эта встреча со " +
                        "странным босоногим человеком....."
                )
                .releaseDate(LocalDate.of(1986, 12, 1))
                .duration(135)
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenDescriptionSize201() {
        Film film = Film.builder()
                .name("Кин-дза-дза!")
                .description("Прораб Владимир Николаевич Машков и не подозревал, что обычный путь до универсама " +
                        "за хлебом и макаронами обернется межгалактическим путешествием. А все эта встреча со " +
                        "странным босоногим человеком......"
                )
                .releaseDate(LocalDate.of(1986, 12, 1))
                .duration(135)
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenReleaseDate28December1895() {
        Film film = Film.builder()
                .name("Test name")
                .description("Test description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(135)
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenReleaseDate27December1895() {
        Film film = Film.builder()
                .name("Test name")
                .description("Test description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(135)
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenDurationPositive() {
        Film film = Film.builder()
                .name("Test name")
                .description("Test description")
                .releaseDate(LocalDate.of(1999, 12, 1))
                .duration(1)
                .build();

        Set violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenDurationZero() {
        Film film = Film.builder()
                .name("Test name")
                .description("Test description")
                .releaseDate(LocalDate.of(1999, 12, 1))
                .duration(0)
                .build();

        Set violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

}
