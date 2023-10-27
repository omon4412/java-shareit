package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    Item item = Item.builder()
            .id(1)
            .name("item")
            .description("item")
            .available(true)
            .build();
    ItemDto itemDto = ItemDto.builder()
            .id(2)
            .name("itemDto")
            .description("itemDto")
            .available(false)
            .build();

    @Test
    void toItemDto() {
        ItemDto itemDtoTest = ItemMapper.toItemDto(item);
        assertEquals(1, itemDtoTest.getId());
        assertEquals("item", itemDtoTest.getName());
        assertEquals("item", itemDtoTest.getDescription());
        assertTrue(itemDtoTest.getAvailable());
    }

    @Test
    void toItem() {
        Item itemTest = ItemMapper.toItem(itemDto);
        assertEquals(2, itemTest.getId());
        assertEquals("itemDto", itemTest.getName());
        assertEquals("itemDto", itemTest.getDescription());
        assertFalse(itemTest.getAvailable());
    }
}