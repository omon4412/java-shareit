package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "start_date")
    protected LocalDateTime start;

    @Column(name = "end_date")
    protected LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id")
    protected Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id")
    protected User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected BookingStatus status;
}
