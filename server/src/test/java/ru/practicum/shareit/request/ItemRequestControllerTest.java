package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;
    Item item;
    ItemRequest itemRequest;
    ItemRequestDto itemRequestDto;
    User user;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        item = Item.builder()
                .id(1)
                .name("item")
                .description("item")
                .available(true)
                .build();
        user = User.builder()
                .id(1)
                .name("Test")
                .email("Test@test.test")
                .build();
        itemRequest = ItemRequest.builder()
                .requestor(user)
                .created(LocalDateTime.now())
                .description("Desc")
                .id(1)
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .items(new ArrayList<>())
                .created(LocalDateTime.now())
                .id(1)
                .description("Desc")
                .build();
    }

    @Test
    void testFindAllByUserId() throws Exception {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(itemRequestDto);
        when(itemRequestService.findAllByUserId(anyInt()))
                .thenReturn(itemRequestDtoList);

        String result = mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemRequestDtoList), result);
    }

    @Test
    void testFindAll() throws Exception {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(itemRequestDto);
        when(itemRequestService.findAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtoList);

        String result = mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemRequestDtoList), result);
    }

    @Test
    void findById() throws Exception {
        when(itemRequestService.findById(anyInt(), anyInt()))
                .thenReturn(itemRequestDto);

        String result = mvc.perform(get("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemRequestDto), result);
    }

    @Test
    void testAddItem() throws Exception {
        when(itemRequestService.addItem(anyInt(), any()))
                .thenReturn(itemRequest);

        String result = mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(ItemRequestMapper.toItemRequestDto(itemRequest)), result);
    }
}