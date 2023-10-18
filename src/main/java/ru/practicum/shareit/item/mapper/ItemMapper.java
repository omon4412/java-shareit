/**
 * Утилитарный класс для преобразования объектов типа Item и связанных DTO.
 */
package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

public class ItemMapper {

    /**
     * Преобразует объект типа Item в объект типа ItemDto.
     *
     * @param item Объект типа Item для преобразования
     * @return Объект типа ItemDto
     */
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .comments(new ArrayList<>())
                .build();
    }

    /**
     * Преобразует объект типа ItemDto в объект типа Item.
     *
     * @param itemDto Объект типа ItemDto для преобразования
     * @return Объект типа Item
     */
    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}
