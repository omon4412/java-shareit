package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userStorage;

    @Transactional
    @Override
    public User addUser(User user) {
        if (userStorage.findByEmail(user.getEmail()) != null) {
            log.error("Пользователь с почтой=" + user.getEmail() + " уже существует");
            try {
                userStorage.save(user);
            } catch (RuntimeException ex) {
                throw new UserAlreadyExistsException("Пользователь с почтой=" + user.getEmail() + " уже существует");
            }
        }
        return userStorage.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(int userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        int userId = user.getId();
        User checkUser = userStorage.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        if (user.getEmail() != null && !user.getEmail().equals(checkUser.getEmail())) {
            if (userStorage.findByEmail(user.getEmail()) != null) {
                log.error("Пользователь с почтой=" + user.getEmail() + " уже существует");
                throw new UserAlreadyExistsException("Пользователь с почтой=" + user.getEmail() + " уже существует");
            }
            checkUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            checkUser.setName(user.getName());
        }
        return userStorage.save(checkUser);
    }

    @Transactional
    @Override
    public User deleteUser(int userId) {
        User checkUser = userStorage.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });
        userStorage.delete(checkUser);
        return checkUser;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<User> getAll() {
        return userStorage.findAll();
    }
}
