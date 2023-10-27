package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    /**
     * Сервис управления предметами.
     */
    private final ItemClient itemClient;

    /**
     * Добавление нового предмета.
     *
     * @param itemDto Данные нового предмета
     * @return Данные добавленного предмета
     */
    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.debug("Начато добавление предмета - " + itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    /**
     * Получение предмета по идентификатору.
     *
     * @param itemId Идентификатор предмета
     * @return Данные предмета
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @PathVariable @PositiveOrZero int itemId) {
        log.debug("Начат поиск предмета - " + itemId);
        return itemClient.getItem(itemId, userId);
    }

    /**
     * Обновление данных предмета.
     *
     * @param itemId  Идентификатор предмета, данные которого нужно обновить
     * @param itemDto Новые данные предмета
     * @return Обновленные данные предмета
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                             @PathVariable int itemId,
                                             @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.debug("Начато обновление предмета - " + itemDto);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    /**
     * Удаление предмета по идентификатору.
     *
     * @param itemId Идентификатор предмета, который нужно удалить
     * @return Данные удаленной предмета
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable @PositiveOrZero int itemId) {
        log.debug("Начато удаление предмети - " + itemId);
        return itemClient.deleteItem(itemId);
    }

    /**
     * Получение списка всех предметов пользователя.
     *
     * @return Список предметов
     */
    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "5") @Positive Integer size) {
        return itemClient.getItems(userId, from, size);
    }

    /**
     * Поиск предмета по названию или описанию.
     *
     * @param text Текст поиска
     * @return список предметов
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam @NotNull String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "5") @Positive Integer size) {
        return itemClient.searchItem(text, from, size);
    }

    /**
     * Добавить комментарий к предмету.
     *
     * @param userId     Пользователь, который оставляет комментарий
     * @param itemId     Предмет, к которому пользователь оставляет комментарий
     * @param commentDto Комментарий
     * @return Добавленный комментарий
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader(value = "X-Sharer-User-Id") Integer userId,
            @PathVariable int itemId,
            @Validated(Create.class) @RequestBody CommentDto commentDto) {
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
