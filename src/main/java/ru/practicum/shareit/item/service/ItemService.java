package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
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
    ItemDto addItem(ItemDto item, int ownerId);

    /**
     * Получает предмет по его идентификатору.
     *
     * @param itemId Идентификатор предмета
     * @param userId
     * @return Объект предмет
     */
    ItemDto getItemById(int itemId, Integer userId);

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
     * @param from
     * @param size
     * @return Список предметов
     */
    Collection<ItemDto> getAll(int userId, Integer from, Integer size);

    /**
     * Поиск предмета по названию или описанию.
     *
     * @param text текст поиска
     * @param from
     * @param size
     * @return список предметов
     */
    Collection<Item> searchItems(String text, Integer from, Integer size);

    /**
     * Добавить комментарий к предмету.
     *
     * @param userId  Пользователь, который оставляет комментарий
     * @param itemId  Предмет, к которому пользователь оставляет комментарий
     * @param comment Комментарий
     * @return Добавленный комментарий
     */
    Comment addComment(Integer userId, int itemId, Comment comment);
}
