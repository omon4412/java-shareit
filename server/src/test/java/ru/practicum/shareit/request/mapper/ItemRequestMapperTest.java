package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestMapperTest {
    @Test
    void testToItemRequestDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1)
                .description("Request description")
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    void testToItemRequest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("Request description")
                .build();

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        assertEquals(itemRequestDto.getId(), itemRequest.getId());
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
    }
}