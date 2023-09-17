package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * Интерфейс для управления пользователями.
 */
public interface UserService {
    /**
     * Добавляет нового пользователя.
     *
     * @param user Объект пользователя для добавления
     * @return Добавленный объект пользователя
     */
    User addUser(User user);

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя
     * @return Объект пользователя
     */
    User getUser(int userId);

    /**
     * Обновляет данные пользователя.
     *
     * @param user Объект пользователя для обновления
     * @return Обновленный объект пользователя
     */
    User updateUser(User user);

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя для удаления
     * @return Удаленный объект пользователя
     */
    User deleteUser(int userId);

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей
     */
    Collection<User> getAll();
}
