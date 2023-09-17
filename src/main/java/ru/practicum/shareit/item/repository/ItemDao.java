package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

/**
 * Интерфейс для доступа к данным о вещах.
 */
public interface ItemDao {
    /**
     * Сохраняет вещь в репозиторий.
     *
     * @param item Объект вещь для сохранения
     * @return Сохраненный объект вещи
     */
    Item save(Item item);

    /**
     * Обновляет данные вещи в репозитории.
     *
     * @param item Объект вещи для обновления
     * @return Обновленный объект вещи
     */
    Item update(Item item);

    /**
     * Получает вещь по его идентификатору.
     *
     * @param itemId Идентификатор вещи
     * @return Объект вещи
     */
    Item get(int itemId);

    /**
     * Удаляет вещь по её идентификатору.
     *
     * @param itemId Идентификатор вещи для удаления
     * @return Удаленный объект вещи
     */
    Item delete(int itemId);

    /**
     * Получает список всех вещей.
     *
     * @param userId Идентификатор пользователя
     * @return Список вещей
     */
    Collection<Item> getAll(int userId);

    Collection<Item> searchItems(String text);
}
