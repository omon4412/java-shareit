package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.WrongOwnerException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {
    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;
    Item item;
    ItemDto itemDto;
    User user;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                commentRepository,
                bookingRepository,
                itemRequestRepository
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
    }

    @Test
    void testAddItem_whenAllCorrect_thenAddItem() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDto id = ItemMapper.toItemDto(item);
        ItemDto foundItem = itemService.addItem(id, user.getId());

        assertNotNull(foundItem);
        assertEquals(item.getId(), foundItem.getId());
        assertEquals(id.getName(), foundItem.getName());
        assertEquals(id.getDescription(), foundItem.getDescription());
        assertEquals(id.getAvailable(), foundItem.getAvailable());
        assertEquals(id.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void testAddItem_whenRequestNotFound_thenThrowRequestNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        ItemDto id = ItemMapper.toItemDto(item);
        id.setRequestId(99);

        RequestNotFoundException thrown = assertThrows(
                RequestNotFoundException.class,
                () -> itemService.addItem(id, user.getId())
        );

        assertTrue(thrown.getMessage().contains("Запрос не найден"));
    }

    @Test
    void testAddItem_whenUserNotFound_thenThrowUserNotFoundException() {
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class,
                () -> itemService.addItem(itemDto, 99));

        assertEquals("Пользователь с id=99 не найден", exception.getMessage());
    }

    @Test
    void testGetItemById_whenItemNotFound_thenThrowItemNotFoundException() {
        Exception exception = assertThrows(ItemNotFoundException.class,
                () -> itemService.getItemById(item.getId(), 99));

        assertEquals("Предмет с id=1 не найден", exception.getMessage());
    }

    @Test
    void testGetItemById_whenAllCorrect_thenReturnItem() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        ItemDto foundedItem = itemService.getItemById(item.getId(), 99);

        assertEquals(item.getId(), foundedItem.getId());
        assertEquals(item.getName(), foundedItem.getName());
        assertEquals(item.getDescription(), foundedItem.getDescription());
    }

    @Test
    void testGetItemById_whenAllCorrect_thenReturnItem111() {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        when(bookingRepository.findLastBooking(1, 1, LocalDateTime.now(), BookingStatus.APPROVED))
                .thenReturn(Collections.emptyList());

        when(bookingRepository.findNextBooking(1, 1, LocalDateTime.now(), BookingStatus.APPROVED))
                .thenReturn(Collections.emptyList());

        ItemDto foundItem = itemService.getItemById(itemDto.getId(), user.getId());
        assertEquals(item.getId(), foundItem.getId());
        assertEquals(item.getName(), foundItem.getName());
        assertEquals(item.getDescription(), foundItem.getDescription());
    }

    @Test
    void testUpdate_whenItemNotFound_thenThrowItemNotFoundException() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem(item, 99));

        assertEquals("Предмет с id=1 не найден", exception.getMessage());
    }

    @Test
    void testUpdate_whenUserNotFound_thenThrowUserNotFoundException() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> itemService.updateItem(item, 99));

        assertEquals("Пользователь с id=99 не найден", exception.getMessage());
    }

    @Test
    void testUpdate_whenUserIsNotOwner_thenThrowWrongOwnerException() {
        User notOwner = User.builder()
                .id(99)
                .email("email")
                .name("test")
                .build();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(notOwner));

        Exception exception = assertThrows(WrongOwnerException.class,
                () -> itemService.updateItem(item, 99));

        assertEquals("Доступ запрещён", exception.getMessage());
    }

    @Test
    void testUpdateUser_whenNothingChanges_thenReturnItem() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(item);

        Item updateItem = itemService.updateItem(item, user.getId());

        assertEquals(item, updateItem);
    }

    @Test
    void testUpdateUser_whenNameChanges_thenReturnUpdatedItem() {
        Item updatedData = Item.builder()
                .id(item.getId())
                .name("New name")
                .build();
        Item newItem = Item.builder()
                .name("New name")
                .id(item.getId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .owner(item.getOwner())
                .build();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(newItem);

        Item updateItem = itemService.updateItem(updatedData, user.getId());

        assertEquals(newItem, updateItem);
    }

    @Test
    void testUpdateUser_whenDescriptionChanges_thenReturnUpdatedItem() {
        Item updatedData = Item.builder()
                .id(item.getId())
                .available(false)
                .build();
        Item newItem = Item.builder()
                .name(item.getName())
                .id(item.getId())
                .available(false)
                .description(item.getDescription())
                .owner(item.getOwner())
                .build();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(newItem);

        Item updateItem = itemService.updateItem(updatedData, user.getId());

        assertEquals(newItem, updateItem);
    }

    @Test
    void testUpdateUser_whenAvailableChanges_thenReturnUpdatedItem() {
        Item updatedData = Item.builder()
                .id(item.getId())
                .description("New desc")
                .build();
        Item newItem = Item.builder()
                .name(item.getName())
                .id(item.getId())
                .available(item.getAvailable())
                .description("New desc")
                .owner(item.getOwner())
                .build();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(newItem);

        Item updateItem = itemService.updateItem(updatedData, user.getId());

        assertEquals(newItem, updateItem);
    }

    @Test
    void testDeleteItem_whenItemNotFound_thenThrowItemNotFoundException() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ItemNotFoundException.class,
                () -> itemService.deleteItem(item.getId()));

        assertEquals("Предмет с id=1 не найден", exception.getMessage());
    }

    @Test
    void testDeleteItem_whenAllCorrect_thenDeleteItem() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        Item deletedItem = itemService.deleteItem(item.getId());

        assertEquals(item, deletedItem);
    }

    @Test
    void testSearchItems_whenTextIsBlank_thenReturnEmptyCollection() {
        Collection<Item> result = itemService.searchItems("", 0, 10);
        assertEquals(0, result.size());
    }

    @Test
    void testSearchItems_whenAllCorrect_thenReturnCollectionWithOneItem() {
        Item item1 = Item.builder()
                .id(1)
                .name("item")
                .description("desc")
                .owner(user)
                .available(true)
                .build();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        Page<Item> page = new PageImpl<>(itemList);

        when(itemRepository.search(anyString(), any())).thenReturn(page);
        Collection<Item> result = itemService.searchItems("item", 0, 10);
        assertEquals(1, result.size());
        assertEquals(page.getContent(), result);
    }

    @Test
    void testSearchItems_whenAllCorrect_thenReturnCollectionWithTwoItem() {
        Item item1 = Item.builder()
                .id(1)
                .name("item")
                .description("desc")
                .owner(user)
                .available(true)
                .build();
        Item item2 = Item.builder()
                .id(2)
                .name("item")
                .description("dfgdfg")
                .owner(user)
                .available(true)
                .build();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        Page<Item> page = new PageImpl<>(itemList);

        when(itemRepository.search(anyString(), any())).thenReturn(page);
        Collection<Item> result = itemService.searchItems("item", 0, 10);
        assertEquals(2, result.size());
        assertEquals(page.getContent(), result);
    }

    @Test
    void testAddComment_whenUserNotFound_thenThrowUserNotFoundException() {
        Comment comment = Comment.builder().build();
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> itemService.addComment(99, 1, comment));

        assertEquals("Пользователь с id=99 не найден", exception.getMessage());
    }

    @Test
    void testAddComment_whenItemNotFound_thenThrowItemNotFoundException() {
        Comment comment = Comment.builder().build();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ItemNotFoundException.class,
                () -> itemService.addComment(1, 99, comment));

        assertEquals("Предмет с id=99 не найден", exception.getMessage());
    }

    @Test
    void testAddComment_whenUserNotMakeBooking_thenThrowBookingBadRequestException() {
        Comment comment = Comment.builder().build();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndItemIdAndStatusIsAndEndBefore(anyInt(), anyInt(), any(), any()))
                .thenReturn(Collections.emptyList());

        Exception exception = assertThrows(BookingBadRequestException.class,
                () -> itemService.addComment(1, 2, comment));

        assertEquals("Пользователь с ID=1 не брал предмет в аренду или срок завершения аренды не наступил",
                exception.getMessage());
    }

    @Test
    void testAddComment_whenUserNotMakeBooking_thenThrowBookingBadRequestException2() {
        Comment comment = Comment.builder()
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();
        Booking booking = new Booking();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndItemIdAndStatusIsAndEndBefore(anyInt(), anyInt(), any(), any()))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any())).thenReturn(comment);

        Comment newComment = itemService.addComment(1, 2, comment);

        assertEquals(comment, newComment);
    }

    @Test
    void testGetAll_whenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception exception = assertThrows(UserNotFoundException.class,
                () -> itemService.getAll(99, 0, 10));

        assertEquals("Пользователь с id=99 не найден", exception.getMessage());
    }

    @Test
    void testGetAll_whenUserNotFound0() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Item item1 = Item.builder()
                .id(1)
                .name("item")
                .description("desc")
                .owner(user)
                .available(true)
                .build();
        Item item2 = Item.builder()
                .id(2)
                .name("item")
                .description("dfgdfg")
                .owner(user)
                .available(true)
                .build();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        Page<Item> page = new PageImpl<>(itemList);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(itemRepository.getAllByOwnerIdOrderByOwnerId(1, pageRequest)).thenReturn(page);

        List<ItemDto> items = new ArrayList<>(itemService.getAll(1, 0, 10));

        assertEquals(2, items.size());
        assertEquals(ItemMapper.toItemDto(item1), items.get(0));
        assertEquals(ItemMapper.toItemDto(item2), items.get(1));
    }
}







