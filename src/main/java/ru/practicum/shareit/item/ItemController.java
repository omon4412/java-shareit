package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    /**
     * Сервис управления вещами.
     */
    private final ItemService itemService;

    /**
     * Добавление новой вещи.
     *
     * @param itemDto Данные новой вещи
     * @return Данные добавленной вещи
     */
    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.debug("Начато добавление вещи - " + itemDto);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemService.addItem(item, userId));
    }

    /**
     * Получение вещи по идентификатору.
     *
     * @param itemId Идентификатор вещи
     * @return Данные вещи
     */
    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable @PositiveOrZero int itemId) {
        log.debug("Начат поиск вещи - " + itemId);
        return ItemMapper.toItemDto(itemService.getItem(itemId));
    }

    /**
     * Обновление данных вещи.
     *
     * @param itemId  Идентификатор вещи, данные которой нужно обновить
     * @param itemDto Новые данные вещи
     * @return Обновленные данные вещи
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                              @PathVariable int itemId,
                              @RequestBody ItemDto itemDto) {
        log.debug("Начато обновление вещи - " + itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        return ItemMapper.toItemDto(itemService.updateItem(item, userId));
    }

    /**
     * Удаление вещи по идентификатору.
     *
     * @param itemId Идентификатор вещи, которую нужно удалить
     * @return Данные удаленной вещи
     */
    @DeleteMapping("/{itemId}")
    public ItemDto deleteItem(@PathVariable @PositiveOrZero int itemId) {
        log.debug("Начато удаление вещи - " + itemId);
        return ItemMapper.toItemDto(itemService.deleteItem(itemId));
    }

    /**
     * Получение списка всех вещей пользователя.
     *
     * @return Список вещей
     */
    @GetMapping
    public Collection<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        Collection<ItemDto> items = itemService.getAll(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.debug("Количество вещей пользователя - " + userId + " - " + items.size());
        return items;
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam @NotNull String text) {
        return itemService.searchItems(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
