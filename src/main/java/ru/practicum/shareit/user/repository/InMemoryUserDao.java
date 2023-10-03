package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class InMemoryUserDao implements UserDao {

    private int lastId;

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        int userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        log.debug("Пользователь - " + user + "добавлен");
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.debug("Пользователь - " + user + "обновлён");
        return users.get(user.getId());
    }

    @Override
    public User get(int userId) {
        User user = users.get(userId);
        log.debug("Пользователь - " + user + "получен");
        return user;
    }

    @Override
    public User delete(int userId) {
        User user = users.get(userId);
        users.remove(userId);
        log.debug("Пользователь - " + user + "удалён");
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public boolean existsByEmail(User user) {
        Optional<User> userWithSameEmail = users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst();
        return userWithSameEmail.isPresent();
    }

    private int generateId() {
        return ++lastId;
    }
}
