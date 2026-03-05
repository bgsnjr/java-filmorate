package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mpa.MpaResponseDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaRepository mpaRepository;

    public List<MpaResponseDto> findAllRatings() {
        return mpaRepository.findAllRatings()
                .stream()
                .map(MpaMapper::toDto)
                .toList();
    }

    public MpaResponseDto findRatingById(Integer id) {
        return MpaMapper.toDto(getMpaOrThrow(id));
    }

    private MPA getMpaOrThrow(Integer id) {
        return mpaRepository.findRatingById(id)
                .orElseThrow(() ->
                        new NotFoundException("Rating with id " + id + " not found"));
    }
}
