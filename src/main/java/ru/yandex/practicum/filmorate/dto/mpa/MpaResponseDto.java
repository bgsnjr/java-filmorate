package ru.yandex.practicum.filmorate.dto.mpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MpaResponseDto {
    private Integer id;
    private String name;
}
