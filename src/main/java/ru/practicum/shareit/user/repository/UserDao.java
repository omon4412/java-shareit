package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

/**
 * Интерфейс для доступа к данным о пользователях.
 */
public interface UserDao {
    /**
     * Сохраняет пользователя в репозитории.
     *
     * @param user Объект пользователя для сохранения
     * @return Сохраненный объект пользователя
     */
    User save(User user);

    /**
     * Обновляет данные пользователя в репозитории.
     *
     * @param user Объект пользователя для обновления
     * @return Обновленный объект пользователя
     */
    User update(User user);

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя
     * @return Объект пользователя
     */
    User get(int userId);

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя для удаления
     * @return Удаленный объект пользователя
     */
    User delete(int userId);

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей
     */
    Collection<User> getAll();

    /**
     * Проверяет, существует ли пользователь с указанным адресом электронной почты.
     *
     * @param user Объект пользователя, для которого выполняется проверка
     * @return true, если пользователь существует по указанному
     * адресу электронной почты, иначе false.
     */
    boolean existsByEmail(User user);
}
