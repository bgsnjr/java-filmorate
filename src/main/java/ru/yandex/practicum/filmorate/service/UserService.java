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
import java.util.Optional;

import static ru.yandex.practicum.filmorate.model.FriendshipStatus.CONFIRMED;
import static ru.yandex.practicum.filmorate.model.FriendshipStatus.PENDING;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto createUser(UserCreateDto dto) {
        User user = userRepository.createUser(UserMapper.toModel(dto));

        log.debug("Created user with id {}", user.getId());

        return UserMapper.toDto(user);
    }

    public UserResponseDto updateUser(UserUpdateDto dto) {
        User user = userRepository.updateUser(UserMapper.toModel(dto));

        log.debug("Updated user with id {}", user.getId());

        return UserMapper.toDto(user);
    }

    public UserResponseDto findUserById(Long id) {
        return UserMapper.toDto(getUserOrThrow(id));
    }

    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAllUsers()
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

        Optional<Integer> directStatusId = userRepository.getFriendshipStatus(userId, friendId);

        if (directStatusId.isPresent()) {
            log.debug("Friend request is already sent", friendId, userId);
            return;
        }

        Optional<Integer> reversedStatusId = userRepository.getFriendshipStatus(userId, friendId);

        if (reversedStatusId.isPresent() && reversedStatusId.get() == PENDING.getId()) {
            userRepository.updateFriendshipStatus(friendId, userId, CONFIRMED);
            log.debug("User {} accepted friend request from user {}", friendId, userId);
            return;
        }

        if (!reversedStatusId.isPresent()) {
            userRepository.addFriend(userId, friendId, PENDING);
            log.debug("User {} sent friend request to user {}", friendId, userId);
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
        return userRepository.findUserById(id)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + id + " not found"));
    }

}
