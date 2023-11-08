package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, представляющий бронь.
 */
@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    /**
     * Уникальный идентификатор брони.
     */
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Дата и время начала брони.
     */
    @Column(name = "start_date")
    protected LocalDateTime start;

    /**
     * Дата и время окончания брони.
     */
    @Column(name = "end_date")
    protected LocalDateTime end;

    /**
     * Объект, который был забронирован.
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    protected Item item;

    /**
     * Пользователь, который совершил бронирование.
     */
    @ManyToOne
    @JoinColumn(name = "booker_id")
    protected User booker;

    /**
     * Состояние бронирования.
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected BookingStatus status;
}
