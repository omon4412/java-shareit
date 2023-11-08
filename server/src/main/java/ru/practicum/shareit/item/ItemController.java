package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    /**
     * Сервис управления предметами.
     */
    private final ItemService itemService;

    /**
     * Добавление нового предмета.
     *
     * @param itemDto Данные нового предмета
     * @return Данные добавленного предмета
     */
    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.debug("Начато добавление предмета - " + itemDto);
        //Item item = ItemMapper.toItem(itemDto);
        return itemService.addItem(itemDto, userId);
    }

    /**
     * Получение предмета по идентификатору.
     *
     * @param itemId Идентификатор предмета
     * @return Данные предмета
     */
    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable @PositiveOrZero int itemId) {
        log.debug("Начат поиск предмета - " + itemId);
        return itemService.getItemById(itemId, userId);
    }

    /**
     * Обновление данных предмета.
     *
     * @param itemId  Идентификатор предмета, данные которого нужно обновить
     * @param itemDto Новые данные предмета
     * @return Обновленные данные предмета
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                              @PathVariable int itemId,
                              @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.debug("Начато обновление предмета - " + itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        return ItemMapper.toItemDto(itemService.updateItem(item, userId));
    }

    /**
     * Удаление предмета по идентификатору.
     *
     * @param itemId Идентификатор предмета, который нужно удалить
     * @return Данные удаленной предмета
     */
    @DeleteMapping("/{itemId}")
    public ItemDto deleteItem(@PathVariable @PositiveOrZero int itemId) {
        log.debug("Начато удаление предмети - " + itemId);
        return ItemMapper.toItemDto(itemService.deleteItem(itemId));
    }

    /**
     * Получение списка всех предметов пользователя.
     *
     * @return Список предметов
     */
    @GetMapping
    public Collection<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(defaultValue = "5") @Positive Integer size) {
        Collection<ItemDto> items = itemService.getAll(userId, from, size);
        log.debug("Количество предметов пользователя - " + userId + " - " + items.size());
        return items;
    }

    /**
     * Поиск предмета по названию или описанию.
     *
     * @param text Текст поиска
     * @return список предметов
     */
    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam @NotNull String text,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "5") @Positive Integer size) {
        return itemService.searchItems(text.toLowerCase(), from, size).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
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
    public CommentDto addComment(
            @RequestHeader(value = "X-Sharer-User-Id") Integer userId,
            @PathVariable int itemId,
            @Validated(Create.class) @RequestBody CommentDto commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);
        return CommentMapper.toCommentDto(itemService.addComment(userId, itemId, comment));
    }
}
