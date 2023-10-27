package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    UserService userService;
    UserRepository userRepository;

    User user;
    User user2;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        user = User.builder()
                .id(1)
                .name("Test")
                .email("Test@test.test")
                .build();
        user2 = User.builder()
                .id(2)
                .name("Test2")
                .email("Test2@test.test")
                .build();
    }

    @Test
    void testAddUser_whenCorrect_thenAddUser() {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        User foundUser = userService.addUser(user);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void testAddUser_whenEmailIsExists_thenThrowUserAlreadyExistsException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(user);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(UserAlreadyExistsException.class);

        UserAlreadyExistsException thrown = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.addUser(user)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с почтой=Test@test.test уже существует"));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testUpdateUser_whenUserNotFound_thenThrowUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(user);

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(user)
        );
        assertTrue(thrown.getMessage().contains("Пользователь с id=1 не найден"));
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
    }

    @Test
    void testUpdateUser_whenEmailIsNotUniq_thenThrowUserAlreadyExistsException() {
        User userTest = User.builder()
                .id(1)
                .email("Test@test.test")
                .name("Test")
                .build();

        user.setEmail("Test2@test.test");

        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(userTest));

        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(user2);

        UserAlreadyExistsException thrown = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.updateUser(user)
        );
        assertTrue(thrown.getMessage().contains("Пользователь с почтой=Test2@test.test уже существует"));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("Test2@test.test");
    }

    @Test
    void testUpdateUser_whenUserNameNotNull_setNewName() {
        User expectedUser = User.builder()
                .id(1)
                .email("Test@test.test")
                .name("New")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(expectedUser);

        user.setName("New");

        userService.updateUser(user);

        assertTrue(expectedUser.getName().contains(user.getName()));
    }

    @Test
    void testUpdateUser_whenUserEmailNotNull_setNewEmail() {
        User expectedUser = User.builder()
                .id(1)
                .email("New@test.test")
                .name("Test")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(expectedUser);

        user.setEmail("New@test.test");

        userService.updateUser(user);

        assertTrue(expectedUser.getName().contains(user.getName()));
    }

    @Test
    void testGetUser_whenUserNotFound_thenThrowUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(user.getId())
        );
        assertTrue(thrown.getMessage().contains("Пользователь с id=1 не найден"));
    }

    @Test
    void testGetUser_whenUserIsFound_thenReturnUser() {
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        User returnedUser = userService.getUser(user.getId());

        assertEquals(user, returnedUser);
    }

    @Test
    void testDeleteUser_whenUserNotFound_thenThrowUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(user.getId())
        );
        assertTrue(thrown.getMessage().contains("Пользователь с id=1 не найден"));
    }

    @Test
    void testDeleteUser_whenUserIsFound_thenReturnUser() {
        Mockito.when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        User returnedUser = userService.deleteUser(user.getId());

        assertEquals(user, returnedUser);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    void testGetAll_whenRepositoryIsEmpty_thenReturnEmptyList() {
        Mockito.when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        Collection<User> userList = userService.getAll();
        assertEquals(0, userList.size());
    }

    @Test
    void testGetAll_whenRepositoryHaveOneUser_thenReturnListWithOneUser() {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user));

        Collection<User> userList = userService.getAll();
        assertEquals(1, userList.size());
        assertEquals(user, new ArrayList<>(userList).get(0));
    }

    @Test
    void testGetAll_whenRepositoryHaveTwoUsers_thenReturnListWithTwoUsers() {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user, user2));

        Collection<User> userList = userService.getAll();
        assertEquals(2, userList.size());
        assertEquals(user, new ArrayList<>(userList).get(0));
        assertEquals(user2, new ArrayList<>(userList).get(1));
    }
}