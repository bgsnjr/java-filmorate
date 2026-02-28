package ru.yandex.practicum.filmorate.model;

public enum Rating {
    G(1),
    PG(2),
    PG13(3),
    R(4),
    NC17(5);

    private final Integer id;

    Rating(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
