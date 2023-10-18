package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;
    Item item;
    ItemDto itemDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        item = Item.builder()
                .id(1)
                .name("item")
                .description("item")
                .available(true)
                .build();
        itemDto = ItemDto.builder()
                .name("itemDto")
                .description("itemDto")
                .available(true)
                .comments(new ArrayList<>())
                .build();
    }

    @Test
    void testAddItem_whenAllCorrect_thenReturnItem() throws Exception {
        when(itemService.addItem(any(), anyInt()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void testAddItem_whenUserIdHeaderIsEmpty_thenReturnBadRequest() throws Exception {
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddItem_whenNameIsNull_thenReturnBadRequest() throws Exception {
        itemDto.setName(null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddItem_whenDescriptionIsNull_thenReturnBadRequest() throws Exception {
        itemDto.setDescription(null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddItem_whenAvailableIsNull_thenReturnBadRequest() throws Exception {
        itemDto.setAvailable(null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetItem_whenAllCorrect_thenReturnItem() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemDto);

        String result = mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemDto), result);
    }

    @Test
    void testGetItem_whenIdIsWrong_thenReturnBadRequest() throws Exception {
        mvc.perform(get("/items/{itemId}", "dfgfdgdfg"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_whenAllCorrect_thenReturnItem() throws Exception {

        when(itemService.updateItem(any(), anyInt())).thenReturn(ItemMapper.toItem(itemDto));

        String result = mvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemDto), result);
    }

    @Test
    void deleteUser() throws Exception {
        when(itemService.deleteItem(anyInt()))
                .thenReturn(item);
        String result = mvc.perform(delete("/items/{id}", item.getId())
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(mapper.writeValueAsString(ItemMapper.toItemDto(item)), result);
    }

    @Test
    void testGetAll_whenItemsIsEmpty_thenReturnEmptyList() throws Exception {
        when(itemService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        String result = mvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("[]", result);
    }

    @Test
    void testGetAll_whenOneItem_thenReturnListWithOneItem() throws Exception {
        when(itemService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        String result = mvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(itemDto)), result);
    }

    @Test
    void testGetAll_whenTwoItems_thenReturnListWithTwoItems() throws Exception {
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Test 2")
                .description("sda")
                .available(true)
                .build();
        when(itemService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto, itemDto2));

        String result = mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(itemDto, itemDto2)), result);
    }

    @Test
    public void testSearchItems_whenNoOneFits_thenReturnEmptyList() throws Exception {
        String searchText = "asdasdas";
        int from = 0;
        int size = 5;

        when(itemService.searchItems(searchText, from, size)).thenReturn(Collections.emptyList());

        String result = mvc.perform(get("/items/search")
                        .param("text", searchText)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("[]", result);
    }

    @Test
    public void testSearchItems_whenOneItemFits_thenReturnListOfItems() throws Exception {
        String searchText = "item";
        int from = 0;
        int size = 5;

        when(itemService.searchItems(searchText, from, size)).thenReturn(List.of(item));

        String result = mvc.perform(get("/items/search")
                        .param("text", searchText)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(ItemMapper.toItemDto(item))), result);
    }

    @Test
    public void testAddComment() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        CommentDto commentDto = CommentDto.builder()
                .text("Comment123123")
                .build();
        LocalDateTime now = LocalDateTime.now();
        Comment comment = Comment.builder()
                .text("Comment123123")
                .created(now)
                .item(item)
                .author(User.builder().build())
                .build();

        when(itemService.addComment(anyInt(), anyInt(), any())).thenReturn(comment);

        String result = mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString((commentDto))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(CommentMapper.toCommentDto(comment)), result);
    }
}