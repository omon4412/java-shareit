package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, представляет собой запросов на предметы.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    /**
     * Уникальный идентификатор запроса.
     */
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    /**
     * Создатель запроса (пользователь).
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    /**
     * Описание запроса.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Дата и время создания запроса.
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
