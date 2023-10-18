package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {

    ItemRequestService itemRequestService;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    Item item;
    ItemDto itemDto;
    User user;

    ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(
                userRepository,
                itemRequestRepository,
                itemRepository
        );
        user = User.builder()
                .id(1)
                .name("Test")
                .email("Test@test.test")
                .build();
        item = Item.builder()
                .id(1)
                .name("item")
                .description("item")
                .owner(user)
                .available(true)
                .build();
        itemDto = ItemDto.builder()
                .name("itemDto")
                .description("itemDto")
                .available(true)
                .build();
        itemRequest = ItemRequest.builder()
                .id(1)
                .created(LocalDateTime.now())
                .description("Desc")
                .requestor(user)
                .build();
    }

    @Test
    void testAddItem_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.addItem(99, itemRequest)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=99 не найден"));
    }

    @Test
    void testAddItem_whenAllCorrect_thenReturnItemRequest() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);

        ItemRequest newItemRequest = itemRequestService.addItem(1, itemRequest);

        assertEquals(itemRequest, newItemRequest);
    }

    @Test
    void testFindAllByUserId_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.findAllByUserId(99)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=99 не найден"));
    }

    @Test
    void testFindAllByUserId_whenAllCorrect_thenReturnListOfItemRequestDto() {
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequestorIdOrderByCreated(anyInt()))
                .thenReturn(itemRequestList);

        Collection<ItemRequestDto> itemRequests = itemRequestService.findAllByUserId(99);

        assertEquals(itemRequestList.stream().map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList()), new ArrayList<>(itemRequests));
    }

    @Test
    void testFindAll_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.findAll(99, 0, 10)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=99 не найден"));
    }

    @Test
    void testFindAll_whenAllCorrect_thenReturnListOfItemRequestDto() {
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        Page<ItemRequest> page = new PageImpl<>(itemRequestList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findRequestsCreatedByOthersOrderByCreated(any(), any(Pageable.class)))
                .thenReturn(page);

        Collection<ItemRequestDto> itemRequests = itemRequestService.findAll(99, 0, 10);

        assertEquals(page.getContent().stream().map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList()), new ArrayList<>(itemRequests));
    }

    @Test
    void testFindById_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.findById(99, 1)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=99 не найден"));
    }

    @Test
    void testFindById_whenItemRequestNotFound_thenThrowRequestNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        RequestNotFoundException thrown = assertThrows(
                RequestNotFoundException.class,
                () -> itemRequestService.findById(99, 1)
        );

        assertTrue(thrown.getMessage().contains("Запрос с id=1 не найден"));
    }

    @Test
    void testFindById_whenAllCorrect_thenReturnItemRequestDto() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(itemRequest));

        ItemRequestDto foundedItemDtoRequest = itemRequestService.findById(99, 1);

        assertEquals(ItemRequestMapper.toItemRequestDto(itemRequest), foundedItemDtoRequest);
    }
}