package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {
    BookingService bookingService;
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    Item item;
    ItemDto itemDto;
    User user;
    Booking booking;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(
                bookingRepository,
                userRepository,
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
        booking = Booking.builder()
                .id(1)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(3))
                .booker(user)
                .item(item)
                .build();
    }

    @Test
    void testSave_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> bookingService.save(99, booking)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=99 не найден"));
    }

    @Test
    void testSave_whenItemNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        ItemNotFoundException thrown = assertThrows(
                ItemNotFoundException.class,
                () -> bookingService.save(99, booking)
        );

        assertTrue(thrown.getMessage().contains("Предмет с id=1 не найден"));
    }

    @Test
    void testSave_whenUserIsOwner_thenThrowBookingNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));

        BookingNotFoundException thrown = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.save(1, booking)
        );

        assertTrue(thrown.getMessage().contains("Нельзя забронировать предмет у самого себя"));
    }

    @Test
    void testSave_whenItemIsNotAvailable_thenThrowItemNotAvailableException() {
        item.setAvailable(false);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));

        ItemNotAvailableException thrown = assertThrows(
                ItemNotAvailableException.class,
                () -> bookingService.save(99, booking)
        );

        assertTrue(thrown.getMessage().contains("Предмет недоступен для бронирования"));
    }

    @Test
    void testSave_whenDatesNotCorrect_thenThrowBookingBadRequestException() {
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().minusDays(2));
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));

        BookingBadRequestException thrown = assertThrows(
                BookingBadRequestException.class,
                () -> bookingService.save(99, booking)
        );

        assertTrue(thrown.getMessage().contains("Дата окончания брони не может быть раньше даты начала брони"));
    }

    @Test
    void testSave_whenAllCorrect_thenSaveBooking() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        Booking bookingResult = bookingService.save(99, booking);


        assertEquals(booking, bookingResult);
    }

    @Test
    void testChangeStatus_whenUserIsNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> bookingService.changeStatus(99, booking.getId(), true)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=99 не найден"));
    }

    @Test
    void testChangeStatus_whenBookingIsNotFound_thenThrowBookingNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        BookingNotFoundException thrown = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.changeStatus(99, booking.getId(), true)
        );

        assertTrue(thrown.getMessage().contains("Бронь с id=1 не найдена"));
    }

    @Test
    void testChangeStatus_whenUserIsNotOwner_thenThrowBookingNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        BookingNotFoundException thrown = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.changeStatus(2, booking.getId(), true)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=2 не может подтвердить бронь у не своего предмета"));
    }

    @Test
    void testChangeStatus_whenBookingStatusIsNotWaiting_thenThrowBookingBadRequestExceptionn() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        BookingBadRequestException thrown = assertThrows(
                BookingBadRequestException.class,
                () -> bookingService.changeStatus(1, booking.getId(), true)
        );

        assertTrue(thrown.getMessage().contains("Бронь уже подтверждена или отклонена"));
    }

    @Test
    void testChangeStatus_whenApproved_thenReturnUpdatedBooking() {
        Booking expectedBooking = Booking.builder()
                .status(BookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .start(booking.getStart())
                .end(booking.getEnd())
                .id(booking.getId())
                .build();
        booking.setStatus(BookingStatus.WAITING);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any()))
                .thenReturn(expectedBooking);

        Booking bookingUpdated = bookingService.changeStatus(1, booking.getId(), true);

        assertEquals(expectedBooking, bookingUpdated);
    }

    @Test
    void testChangeStatus_whenReject_thenReturnUpdatedBooking() {
        Booking expectedBooking = Booking.builder()
                .status(BookingStatus.REJECTED)
                .booker(user)
                .item(item)
                .start(booking.getStart())
                .end(booking.getEnd())
                .id(booking.getId())
                .build();
        booking.setStatus(BookingStatus.WAITING);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any()))
                .thenReturn(expectedBooking);

        Booking bookingUpdated = bookingService.changeStatus(1, booking.getId(), false);

        assertEquals(expectedBooking, bookingUpdated);
    }

    @Test
    void testGetById_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> bookingService.getById(1, 1)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=1 не найден"));
    }

    @Test
    void testGetById_whenBookingNotFound_thenThrowBookingNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        BookingNotFoundException thrown = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.getById(1, 1)
        );

        assertTrue(thrown.getMessage().contains("Бронь с id=1 не найдена"));
    }

    @Test
    void testGetById_whenUserIsNotOwner_thenThrowBookingNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        BookingNotFoundException thrown = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.getById(2, 1)
        );

        assertTrue(thrown.getMessage().contains("Бронь с id=1 не найдена"));
    }

    @Test
    void testGetById_whenAllCorrect_thenReturnBooking() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        Booking gettingBooking = bookingService.getById(1, 1);

        assertEquals(booking, gettingBooking);
    }

    @Test
    void testGetAll_whenUserIsNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> bookingService.getAll(1, BookingStatus.APPROVED, 0, 10)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=1 не найден"));
    }

    @Test
    void testGetAll_whenStateIsWaiting_thenReturnBooking() {
        booking.setStatus(BookingStatus.WAITING);
        List<Booking> itemList = new ArrayList<>();
        itemList.add(booking);
        Page<Booking> page = new PageImpl<>(itemList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatusIs(anyInt(), any(), any(Pageable.class)))
                .thenReturn(page);

        Collection<Booking> bookingResult = bookingService.getAll(1, BookingStatus.WAITING, 0, 10);

        assertEquals(itemList, bookingResult);
    }

    @Test
    void testGetAll_whenStateIsReject_thenReturnBooking() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> itemList = new ArrayList<>();
        itemList.add(booking);
        Page<Booking> page = new PageImpl<>(itemList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatusIs(anyInt(), any(), any(Pageable.class)))
                .thenReturn(page);

        Collection<Booking> bookingResult = bookingService.getAll(1, BookingStatus.REJECTED, 0, 10);

        assertEquals(itemList, bookingResult);
    }

    @Test
    void testGetAll_whenStateIsAll_thenReturnBooking() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> itemList = new ArrayList<>();
        itemList.add(booking);
        Page<Booking> page = new PageImpl<>(itemList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdOrderByIdDesc(anyInt(), any(Pageable.class)))
                .thenReturn(page);

        Collection<Booking> bookingResult = bookingService.getAll(1, BookingStatus.ALL, 0, 10);

        assertEquals(itemList, bookingResult);
    }

    @Test
    void testGetAll_whenStateIsCurrent_thenReturnBooking() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> itemList = new ArrayList<>();
        itemList.add(booking);
        Page<Booking> page = new PageImpl<>(itemList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(anyInt(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        Collection<Booking> bookingResult = bookingService.getAll(1, BookingStatus.CURRENT, 0, 10);

        assertEquals(itemList, bookingResult);
    }

    @Test
    void testGetAll_whenStateIsPast_thenReturnBooking() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> itemList = new ArrayList<>();
        itemList.add(booking);
        Page<Booking> page = new PageImpl<>(itemList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByIdDesc(anyInt(), any(), any(Pageable.class)))
                .thenReturn(page);

        Collection<Booking> bookingResult = bookingService.getAll(1, BookingStatus.PAST, 0, 10);

        assertEquals(itemList, bookingResult);
    }

    @Test
    void testGetAll_whenStateIsFuture_thenReturnBooking() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> itemList = new ArrayList<>();
        itemList.add(booking);
        Page<Booking> page = new PageImpl<>(itemList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByIdDesc(anyInt(), any(), any(Pageable.class)))
                .thenReturn(page);

        BookingBadRequestException exception = assertThrows(
                BookingBadRequestException.class,
                () -> bookingService.getAll(1, BookingStatus.UNKNOWN, 0, 10)
        );

        assertTrue(exception.getMessage().contains("Unknown state: UNSUPPORTED_STATUS"));
    }

    @Test
    void testGetAllByOwner_whenUserIsNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> bookingService.getAllByOwner(1, BookingStatus.APPROVED, 0, 10)
        );

        assertTrue(thrown.getMessage().contains("Пользователь с id=1 не найден"));
    }

    @Test
    void testGetAllByOwner() {
        booking.setStatus(BookingStatus.WAITING);
        List<Booking> itemList = new ArrayList<>();
        itemList.add(booking);
        Page<Booking> page = new PageImpl<>(itemList);
        when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItemOwnerIdAndStatusIs(anyInt(), any(), any(Pageable.class)))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdOrderByIdDesc(anyInt(), any(Pageable.class)))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyInt(), any(), any(), any(Pageable.class)))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByIdDesc(anyInt(), any(), any(Pageable.class)))
                .thenReturn(page);
        when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByIdDesc(anyInt(), any(), any(Pageable.class)))
                .thenReturn(page);

        Collection<Booking> bookingResultWaiting = bookingService.getAllByOwner(1, BookingStatus.WAITING, 0, 10);
        Collection<Booking> bookingResultReject = bookingService.getAllByOwner(1, BookingStatus.REJECTED, 0, 10);
        Collection<Booking> bookingResultAll = bookingService.getAllByOwner(1, BookingStatus.ALL, 0, 10);
        Collection<Booking> bookingResultCurrent = bookingService.getAllByOwner(1, BookingStatus.CURRENT, 0, 10);
        Collection<Booking> bookingResultPast = bookingService.getAllByOwner(1, BookingStatus.PAST, 0, 10);
        Collection<Booking> bookingResultFuture = bookingService.getAllByOwner(1, BookingStatus.FUTURE, 0, 10);

        BookingBadRequestException exception = assertThrows(
                BookingBadRequestException.class,
                () -> bookingService.getAllByOwner(1, BookingStatus.UNKNOWN, 0, 10)
        );

        assertTrue(exception.getMessage().contains("Unknown state: UNSUPPORTED_STATUS"));

        assertEquals(itemList, bookingResultWaiting);
        assertEquals(itemList, bookingResultReject);
        assertEquals(itemList, bookingResultAll);
        assertEquals(itemList, bookingResultCurrent);
        assertEquals(itemList, bookingResultPast);
        assertEquals(itemList, bookingResultFuture);
    }
}