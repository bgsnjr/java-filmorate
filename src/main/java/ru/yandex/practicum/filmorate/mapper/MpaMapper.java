package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.mpa.MpaResponseDto;
import ru.yandex.practicum.filmorate.model.MPA;

public final class MpaMapper {
    public static MpaResponseDto toDto(MPA mpa) {
        return MpaResponseDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }
}
