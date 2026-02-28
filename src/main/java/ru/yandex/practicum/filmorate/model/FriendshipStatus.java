package ru.yandex.practicum.filmorate.model;

public enum FriendshipStatus {
    PENDING(1),
    CONFIRMED(2);

    private final Integer id;

    FriendshipStatus(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
