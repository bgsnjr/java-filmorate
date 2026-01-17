package ru.yandex.practicum.filmorate.util;

import java.util.Map;

public class IdGenerator {
    public static Long getNextId(Map<?, ?> objectsList) {
        long currentMaxId = objectsList.keySet()
                .stream()
                .mapToLong(id -> (long) id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
