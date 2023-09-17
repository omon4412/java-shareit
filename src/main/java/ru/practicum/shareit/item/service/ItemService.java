package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

/**
 * Интерфейс для управления вещами.
 */
public interface ItemService {
    /**
     * Добавляет новую вещь.
     *
     * @param item Объект вещи для добавления
     * @return Добавленный объект вещи
     */
    Item addItem(Item item, int ownerId);

    /**
     * Получает вещь по её идентификатору.
     *
     * @param itemId Идентификатор вещи
     * @return Объект вещь
     */
    Item getItem(int itemId);

    /**
     * Обновляет данные вещи.
     *
     * @param item Объект вещи для обновления
     * @return Обновленный объект вещи
     */
    Item updateItem(Item item, int userId);

    /**
     * Удаляет вещь по его идентификатору.
     *
     * @param itemId Идентификатор вещи для удаления
     * @return Удаленный объект вещи
     */
    Item deleteItem(int itemId);

    /**
     * Получает список всех вещей.
     *
     * @param userId Идентификатор пользователя
     * @return Список вещей
     */
    Collection<Item> getAll(int userId);

    Collection<Item> searchItems(String text);
}
