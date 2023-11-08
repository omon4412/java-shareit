package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, представляющий модель комментария к предмету.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    /**
     * Текст комментария.
     */
    @Column(name = "text")
    protected String text;

    /**
     * Предмет, к которому привязан комментарий.
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    protected Item item;

    /**
     * Автор комментария.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User author;

    /**
     * Дата и время создания комментария.
     */
    @Column(name = "created")
    protected LocalDateTime created;
}
