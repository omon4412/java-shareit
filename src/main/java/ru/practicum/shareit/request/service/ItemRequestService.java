package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

/**
 * Сервис, предоставляющий методы для работы с запросами на предметы.
 */
public interface ItemRequestService {

    /**
     * Найти все запросы, созданные пользователем.
     *
     * @param userId Уникальный идентификатор пользователя
     * @return Коллекция объектов ItemRequestDto, представляющих найденные запросы
     */
    Collection<ItemRequestDto> findAllByUserId(Integer userId);

    /**
     * Найти все запросы.
     *
     * @param userId Уникальный идентификатор пользователя
     * @param from   Начальный индекс для пагинации
     * @param size   Количество элементов для отображения на странице
     * @return Коллекция объектов ItemRequestDto, представляющих найденные запросы
     */
    Collection<ItemRequestDto> findAll(Integer userId, int from, int size);

    /**
     * Найти запрос по его идентификатору.
     *
     * @param userId    идентификатор пользователя
     * @param requestId идентификатор запроса
     * @return Объект ItemRequestDto, представляющий найденный запрос
     */
    ItemRequestDto findById(Integer userId, Integer requestId);

    /**
     * Создать новый запрос на предмет.
     *
     * @param userId      идентификатор пользователя, создающего запрос
     * @param itemRequest Объект ItemRequest, содержащий данные нового запроса
     * @return Объект ItemRequestDto, представляющий созданный запрос
     */
    ItemRequest addItem(Integer userId, ItemRequest itemRequest);
}
