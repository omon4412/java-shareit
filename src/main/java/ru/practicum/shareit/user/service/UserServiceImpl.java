package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userStorage;

    @Override
    public User addUser(User user) {
        if (userStorage.existsByEmail(user)) {
            log.error("Пользователь с почтой=" + user.getEmail() + " уже существует");
            throw new UserAlreadyExistsException("Пользователь с почтой=" + user.getEmail() + " уже существует");
        }
        return userStorage.save(user);
    }

    @Override
    public User getUser(int userId) {
        User user = userStorage.get(userId);
        if (user == null) {
            log.error("Пользователь с id=" + userId + " не найден");
            throw new UserNotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();
        User checkUser = userStorage.get(userId);
        if (checkUser == null) {
            log.error("Пользователь с id=" + userId + " не найден");
            throw new UserNotFoundException("Пользователь с id=" + userId + " не найден");
        }

        if (user.getEmail() != null && !user.getEmail().equals(checkUser.getEmail())) {
            if (userStorage.existsByEmail(user)) {
                log.error("Пользователь с почтой=" + user.getEmail() + " уже существует");
                throw new UserAlreadyExistsException("Пользователь с почтой=" + user.getEmail() + " уже существует");
            }
            checkUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            checkUser.setName(user.getName());
        }
        return userStorage.update(checkUser);
    }

    @Override
    public User deleteUser(int userId) {
        User user = userStorage.delete(userId);
        if (user == null) {
            log.error("Пользователь с id=" + userId + " не найден");
            throw new UserNotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }
}
