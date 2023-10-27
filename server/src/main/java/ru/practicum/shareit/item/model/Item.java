package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
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
     * Идентификатор предмета.
     */
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Название предмета.
     */
    @Column(name = "name")
    protected String name;

    /**
     * Описание предмета.
     */
    @Column(name = "description")
    protected String description;

    /**
     * Признак доступности предмета.
     */
    @Column(name = "available")
    protected Boolean available;

    /**
     * Владелец предмета.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    protected User owner;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
