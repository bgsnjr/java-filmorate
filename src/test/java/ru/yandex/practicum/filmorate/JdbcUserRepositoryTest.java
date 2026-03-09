package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcUserRepository.class, UserRowMapper.class})
public class JdbcUserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        User created = userRepository.createUser(user);

        assertAll(
                () -> assertNotNull(created.getId()),
                () -> assertEquals(user.getEmail(), created.getEmail()),
                () -> assertEquals(user.getLogin(), created.getLogin()),
                () -> assertEquals(user.getName(), created.getName()),
                () -> assertEquals(user.getBirthday(), created.getBirthday())
        );
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        Long userId = userRepository.createUser(user).getId();

        User userWithNewData = User.builder()
                .id(userId)
                .email("updated@mail.com")
                .login("updated")
                .name("Updated Name")
                .birthday(LocalDate.of(2000, 10, 20)).build();

        User updated = userRepository.updateUser(userWithNewData);

        assertAll(
                () -> assertEquals(userWithNewData.getEmail(), updated.getEmail()),
                () -> assertEquals(userWithNewData.getLogin(), updated.getLogin()),
                () -> assertEquals(userWithNewData.getName(), updated.getName()),
                () -> assertEquals(userWithNewData.getBirthday(), updated.getBirthday())
        );
    }

    @Test
    public void testFindUserById() {
        User user = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        Long userId = userRepository.createUser(user).getId();

        Optional<User> userOptional = userRepository.findUserById(userId);

        assertThat(userOptional).isPresent().hasValueSatisfying(u ->
                assertThat(u).hasFieldOrPropertyWithValue("id", userId)
        );
    }

    @Test
    public void testFindAllUsers() {
        User user1 = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        User user2 = User.builder()
                .email("alice@mail.com")
                .login("adoe")
                .name("Alice Doe")
                .birthday(LocalDate.of(1995, 5, 15)).build();

        Long firstUserId = userRepository.createUser(user1).getId();
        Long secondUserId = userRepository.createUser(user2).getId();

        List<User> users = userRepository.findAllUsers();

        assertAll(
                () -> assertEquals(2, users.size()),
                () -> assertThat(users.stream().map(User::getId).toList())
                        .containsAll(List.of(firstUserId, secondUserId))
        );
    }

    @Test
    public void testAddFriend() {
        User user = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        User friend = User.builder()
                .email("alice@mail.com")
                .login("adoe")
                .name("Alice Doe")
                .birthday(LocalDate.of(1995, 5, 15)).build();

        Long userId = userRepository.createUser(user).getId();
        Long friendId = userRepository.createUser(friend).getId();

        userRepository.addFriend(userId, friendId, FriendshipStatus.PENDING);

        Optional<Integer> friendshipStatus = userRepository.getFriendshipStatus(userId, friendId);

        assertThat(friendshipStatus).isPresent().hasValue(FriendshipStatus.PENDING.getId());

        userRepository.updateFriendshipStatus(userId, friendId, FriendshipStatus.CONFIRMED);

        Optional<Integer> updatedFriendshipStatus = userRepository.getFriendshipStatus(userId, friendId);

        assertThat(updatedFriendshipStatus).isPresent().hasValue(FriendshipStatus.CONFIRMED.getId());
    }

    @Test
    public void testRemoveFriend() {
        User user = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        User friend = User.builder()
                .email("alice@mail.com")
                .login("adoe")
                .name("Alice Doe")
                .birthday(LocalDate.of(1995, 5, 15)).build();

        Long userId = userRepository.createUser(user).getId();
        Long friendId = userRepository.createUser(friend).getId();

        userRepository.addFriend(userId, friendId, FriendshipStatus.PENDING);

        userRepository.removeFriend(userId, friendId);

        Optional<Integer> friendshipStatus = userRepository.getFriendshipStatus(userId, friendId);

        assertThat(friendshipStatus).isEmpty();
    }

    @Test
    public void testFindFriends() {
        User user = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        User friend1 = User.builder()
                .email("alice@mail.com")
                .login("adoe")
                .name("Alice Doe")
                .birthday(LocalDate.of(1995, 5, 15)).build();

        User friend2 = User.builder()
                .email("jsnow@mail.com")
                .login("jsnow")
                .name("John Snow")
                .birthday(LocalDate.of(1998, 12, 28)).build();

        Long userId = userRepository.createUser(user).getId();
        Long firstFriendId = userRepository.createUser(friend1).getId();
        Long secondFriendId = userRepository.createUser(friend2).getId();

        userRepository.addFriend(userId, firstFriendId, FriendshipStatus.PENDING);
        userRepository.addFriend(userId, secondFriendId, FriendshipStatus.PENDING);

        List<User> users = userRepository.findFriends(userId);

        assertAll(
                () -> assertEquals(2, users.size()),
                () -> assertThat(users.stream().map(User::getId).toList())
                        .containsAll(List.of(firstFriendId, secondFriendId))
        );
    }

    @Test
    public void testFindMutualFriends() {
        User user1 = User.builder()
                .email("jd@mail.com")
                .login("jdoe")
                .name("John Doe")
                .birthday(LocalDate.of(1990, 10, 20)).build();

        User user2 = User.builder()
                .email("alice@mail.com")
                .login("adoe")
                .name("Alice Doe")
                .birthday(LocalDate.of(1995, 5, 15)).build();

        User friend1 = User.builder()
                .email("jsnow@mail.com")
                .login("jsnow")
                .name("John Snow")
                .birthday(LocalDate.of(1998, 12, 28)).build();

        User friend2 = User.builder()
                .email("mrain@mail.com")
                .login("mrain")
                .name("Mary Rain")
                .birthday(LocalDate.of(1997, 11, 25)).build();

        Long firstUserId = userRepository.createUser(user1).getId();
        Long secondUserId = userRepository.createUser(user2).getId();
        Long firstFriendId = userRepository.createUser(friend1).getId();
        Long secondFriendId = userRepository.createUser(friend2).getId();

        userRepository.addFriend(firstUserId, firstFriendId, FriendshipStatus.PENDING);
        userRepository.addFriend(secondUserId, firstFriendId, FriendshipStatus.PENDING);
        userRepository.addFriend(secondUserId, secondFriendId, FriendshipStatus.PENDING);

        List<User> mutualFriends = userRepository.findMutualFriends(firstUserId, secondUserId);

        assertAll(
                () -> assertEquals(1, mutualFriends.size()),
                () -> assertThat(mutualFriends.stream().map(User::getId).toList())
                        .containsAll(List.of(firstFriendId))
        );
    }
}
