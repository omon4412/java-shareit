package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Контроллер для обработки HTTP-запросов, связанных с запросами на предметы.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    /**
     * Сервис для работы с запросами.
     */
    private final ItemRequestService itemRequestService;

    /**
     * Получить все запросы, созданные определенным пользователем.
     *
     * @param userId Уникальный идентификатор пользователя
     * @return Коллекция объектов ItemRequestDto, представляющих запросы пользователя
     */
    @GetMapping
    public Collection<ItemRequestDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.findAllByUserId(userId);
    }

    /**
     * Получить все запросы, созданные другими пользователями.
     *
     * @param userId Уникальный идентификатор пользователя
     * @param from   Индекс первого элемента для пагинации
     * @param size   Количество элементов для отображения
     * @return Коллекция объектов ItemRequestDto, представляющих запросы других пользователей
     */
    @GetMapping("/all")
    public Collection<ItemRequestDto> findAll(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "5") @Positive Integer size) {
        return itemRequestService.findAll(userId, from, size);
    }

    /**
     * Получить информацию о запросе по его уникальному идентификатору.
     *
     * @param userId    Уникальный идентификатор пользователя
     * @param requestId Уникальный идентификатор запроса
     * @return Объект ItemRequestDto, представляющий информацию о запросе
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                   @PathVariable Integer requestId) {
        return itemRequestService.findById(userId, requestId);
    }

    /**
     * Создать новый запрос на элемент.
     *
     * @param userId         Уникальный идентификатор пользователя, создающего запрос
     * @param itemRequestDto Объект ItemRequestDto, содержащий данные нового запроса
     * @return Объект ItemRequestDto, представляющий созданный запрос
     */
    @PostMapping
    public ItemRequestDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(itemRequestService.addItem(userId, itemRequest));
    }
}
