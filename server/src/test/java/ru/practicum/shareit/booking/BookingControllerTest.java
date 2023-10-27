package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;
    Item item;

    User user;
    Booking booking;

    BookingDto bookingDto;

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
        booking = Booking.builder()
                .id(1)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(3))
                .booker(user)
                .item(item)
                .build();
        bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(3))
                .id(1)
                .build();
    }

    @Test
    void testSave_whenAllCorrect_thenReturnBooking() throws Exception {
        BookingDto bookingDto1 = BookingMapper.toBookingDto(booking);
        when(bookingService.save(anyInt(), any()))
                .thenReturn(booking);

        String result = mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto1))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(BookingMapper.toBookingResponseDto(booking)), result);
    }

    @Test
    void testChangeStatus() throws Exception {
        booking.setStatus(BookingStatus.REJECTED);
        BookingDto bookingDto1 = BookingMapper.toBookingDto(booking);
        when(bookingService.changeStatus(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(booking);

        String result = mvc.perform(patch("/bookings/{id}?approved=false", bookingDto1.getId())
                        .content(mapper.writeValueAsString(bookingDto1))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(BookingMapper.toBookingResponseDto(booking)), result);
    }

    @Test
    void testGetById() throws Exception {
        when(bookingService.getById(anyInt(), anyInt()))
                .thenReturn(booking);

        String result = mvc.perform(get("/bookings/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(BookingMapper.toBookingResponseDto(booking)), result);
    }

    @Test
    void testGetAll() throws Exception {
        Booking booking1 = Booking.builder()
                .id(2)
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .id(3)
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking1);
        bookings.add(booking2);
        when(bookingService.getAll(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        String result = mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList())), result);
    }

    @Test
    void testGetAll_whenStateIsUnknown() throws Exception {
        Booking booking1 = Booking.builder()
                .id(2)
                .item(item)
                .booker(user)
                .status(BookingStatus.UNKNOWN)
                .build();
        Booking booking2 = Booking.builder()
                .id(3)
                .item(item)
                .booker(user)
                .status(BookingStatus.UNKNOWN)
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        when(bookingService.getAll(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        String result = mvc.perform(get("/bookings?state=sdfsdf")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList())), result);
    }

    @Test
    void getAllByOwner() throws Exception {
        Booking booking1 = Booking.builder()
                .id(2)
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .id(3)
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking1);
        bookings.add(booking2);
        when(bookingService.getAllByOwner(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        String result = mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList())), result);
    }

    @Test
    void getAllByOwner_whenStateIsUnknown() throws Exception {
        Booking booking1 = Booking.builder()
                .id(2)
                .item(item)
                .booker(user)
                .status(BookingStatus.UNKNOWN)
                .build();
        Booking booking2 = Booking.builder()
                .id(3)
                .item(item)
                .booker(user)
                .status(BookingStatus.UNKNOWN)
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        when(bookingService.getAllByOwner(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        String result = mvc.perform(get("/bookings/owner?state=sdfsdf")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList())), result);
    }
}