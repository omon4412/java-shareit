package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * Класс, представляющий предмет.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    /**
     * Идентификатор вещи.
     */
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Название вещи.
     */
    @Column(name = "name")
    protected String name;

    /**
     * Описание вещи.
     */
    @Column(name = "description")
    protected String description;

    /**
     * Признак доступности вещи.
     */
    @Column(name = "available")
    protected Boolean available;

    /**
     * Владелец вещи.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    protected User owner;
}
