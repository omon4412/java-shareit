package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
    private final RequestClient itemRequestClient;

    /**
     * Получить все запросы, созданные определенным пользователем.
     *
     * @param userId Уникальный идентификатор пользователя
     * @return Коллекция объектов ItemRequestDto, представляющих запросы пользователя
     */
    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestClient.getItemRequestsByUser(userId);
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
    public ResponseEntity<Object> findAll(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "5") @Positive Integer size) {
        return itemRequestClient.getAll(userId, from, size);
    }

    /**
     * Получить информацию о запросе по его уникальному идентификатору.
     *
     * @param userId    Уникальный идентификатор пользователя
     * @param requestId Уникальный идентификатор запроса
     * @return Объект ItemRequestDto, представляющий информацию о запросе
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @PathVariable Integer requestId) {
        return itemRequestClient.getItemRequest(userId, requestId);
    }

    /**
     * Создать новый запрос на элемент.
     *
     * @param userId         Уникальный идентификатор пользователя, создающего запрос
     * @param itemRequestDto Объект ItemRequestDto, содержащий данные нового запроса
     * @return Объект ItemRequestDto, представляющий созданный запрос
     */
    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }
}
