package ru.yandex.practicum.filmorate.util;

import java.util.Map;

public class IdGenerator {
    public static Long getNextId(Map<? extends Number, ?> objectsList) {
        long currentMaxId = objectsList.keySet()
                .stream()
                .mapToLong(Number::longValue)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
