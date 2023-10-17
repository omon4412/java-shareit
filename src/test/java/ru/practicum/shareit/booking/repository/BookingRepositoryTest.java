package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    User itemOwner;

    User itemBooker;

    Item item;

    Booking booking;

    @BeforeEach
    void setUp() {
        itemOwner = User.builder()
                .name("itemOwner")
                .email("itemOwner@test.test")
                .build();
        em.persist(itemOwner);

        itemBooker = User.builder()
                .name("itemBooker")
                .email("itemBooker@test.test")
                .build();
        em.persist(itemBooker);

        item = Item.builder()
                .owner(itemOwner)
                .name("Item name")
                .available(true)
                .description("Desc word")
                .build();
        em.persist(item);

        booking = Booking.builder()
                .item(item)
                .booker(itemBooker)
                .start(LocalDateTime.now().minusDays(10))
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.APPROVED)
                .build();
        em.persist(booking);
    }

    @Test
    void testFindLastBooking_ifExists() {
        Booking findedBooking = bookingRepository.findLastBooking(itemOwner.getId(), item.getId(),
                LocalDateTime.now().plusDays(2), BookingStatus.APPROVED).get(0);
        assertEquals(booking, findedBooking);
    }

    @Test
    void testFindLastBooking_ifNotExists() {
        List<Booking> findedBooking = bookingRepository.findLastBooking(itemOwner.getId(), item.getId(),
                LocalDateTime.now().minusDays(11), BookingStatus.APPROVED);
        assertEquals(0, findedBooking.size());
    }

    @Test
    void testFindNextBooking_ifExists() {
        Booking findedBooking = bookingRepository.findNextBooking(itemOwner.getId(), item.getId(),
                LocalDateTime.now().minusDays(11), BookingStatus.APPROVED).get(0);
        assertEquals(booking, findedBooking);
    }

    @Test
    void testFindNextBooking_ifNotExists() {
        List<Booking> findedBooking = bookingRepository.findNextBooking(itemOwner.getId(), item.getId(),
                LocalDateTime.now().plusDays(2), BookingStatus.APPROVED);
        assertEquals(0, findedBooking.size());
    }
}