package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

/**
 * Интерфейс для управления предметами.
 */
public interface ItemService {
    /**
     * Добавляет новый предмет.
     *
     * @param item Объект предмета для добавления
     * @return Добавленный объект предмета
     */
    Item addItem(Item item, int ownerId);

    /**
     * Получает предмет по его идентификатору.
     *
     * @param itemId Идентификатор предмета
     * @return Объект предмет
     */
    Item getItem(int itemId);

    /**
     * Обновляет данные предмета.
     *
     * @param item Объект предмета для обновления
     * @return Обновленный объект предмета
     */
    Item updateItem(Item item, int userId);

    /**
     * Удаляет предмет по его идентификатору.
     *
     * @param itemId Идентификатор предмета для удаления
     * @return Удаленный объект предмета
     */
    Item deleteItem(int itemId);

    /**
     * Получает список всех предметов.
     *
     * @param userId Идентификатор пользователя
     * @return Список предметов
     */
    Collection<Item> getAll(int userId);

    Collection<Item> searchItems(String text);
}
