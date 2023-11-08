/**
 * Утилитарный класс для преобразования объектов типа ItemRequest и связанных DTO.
 */
package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;

public class ItemRequestMapper {

    /**
     * Преобразует объект типа ItemRequest в объект типа ItemRequestDto.
     *
     * @param itemRequest Объект типа ItemRequest для преобразования
     * @return Объект типа ItemRequestDto
     */
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(new ArrayList<>())
                .build();
    }

    /**
     * Преобразует объект типа ItemRequestDto в объект типа ItemRequest.
     *
     * @param itemRequestDto Объект типа ItemRequestDto для преобразования
     * @return Объект типа ItemRequest
     */
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .build();
    }
}
