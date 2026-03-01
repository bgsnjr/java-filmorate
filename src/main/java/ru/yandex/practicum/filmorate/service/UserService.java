package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.user.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;

import static ru.yandex.practicum.filmorate.model.FriendshipStatus.PENDING;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto createUser(UserCreateDto dto) {
        User user = UserMapper.toModel(dto);
        User saved = userRepository.create(user);

        log.debug("Created user with id {}", saved.getId());

        return UserMapper.toDto(saved);
    }

    public UserResponseDto updateUser(UserUpdateDto dto) {
        User existing = getUserOrThrow(dto.getId());

        UserMapper.updateModel(existing, dto);

        User updated = userRepository.update(existing);

        log.debug("Updated user with id {}", updated.getId());

        return UserMapper.toDto(updated);
    }

    public UserResponseDto findUserById(Long id) {
        return UserMapper.toDto(getUserOrThrow(id));
    }

    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public void addFriend(Long userId, Long friendId) {
        getUserOrThrow(userId);
        getUserOrThrow(friendId);

        if (userId.equals(friendId)) {
            throw new ValidationException("Cannot add yourself as a friend");
        }

        if (!userRepository.existsFriendship(userId, friendId)) {
            userRepository.addFriend(userId, friendId, PENDING);
            log.debug("User {} added friend {}", userId, friendId);
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserOrThrow(userId);
        getUserOrThrow(friendId);

        userRepository.removeFriend(userId, friendId);

        log.debug("User {} removed friend {}", userId, friendId);
    }

    public List<UserResponseDto> findFriends(Long userId) {
        getUserOrThrow(userId);

        return userRepository.findFriends(userId)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public List<UserResponseDto> findMutualFriends(Long userId, Long otherUserId) {
        getUserOrThrow(userId);
        getUserOrThrow(otherUserId);

        return userRepository.findMutualFriends(userId, otherUserId)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + id + " not found"));
    }

}
