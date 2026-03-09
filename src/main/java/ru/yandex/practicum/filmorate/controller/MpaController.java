package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.mpa.MpaResponseDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<MpaResponseDto> findAllRatings() {
        return mpaService.findAllRatings();
    }

    @GetMapping("/{id}")
    public MpaResponseDto findRatingById(@PathVariable @Positive Integer id) {
        return mpaService.findRatingById(id);
    }
}
