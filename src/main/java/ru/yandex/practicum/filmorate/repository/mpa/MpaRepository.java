package ru.yandex.practicum.filmorate.repository.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MpaRepository {
    Optional<MPA> findRatingById(Integer id);

    List<MPA> findAllRatings();
}
