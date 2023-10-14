package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * DTO для представления данных о предмете.
 */
@Data
@Builder
public class ItemDto {

    /**
     * Идентификатор предмета.
     */
    protected int id;

    /**
     * Название предмета.
     */
    @NotBlank(groups = {Create.class})
    protected String name;

    /**
     * Описание предмета.
     */
    @NotBlank(groups = {Create.class})
    protected String description;

    /**
     * Признак доступности предмета.
     */
    @NotNull(groups = {Create.class})
    protected Boolean available;

    /**
     * Последняя бронь.
     */
    protected BookingInfo lastBooking;

    /**
     * Следующая бронь.
     */
    protected BookingInfo nextBooking;

    /**
     * Комментарии к предмету.
     */
    protected Collection<CommentDto> comments;

    private Integer requestId;

    /**
     * Информация об одной брони
     */
    @Data
    @AllArgsConstructor
    public static class BookingInfo {
        /**
         * Идентификатор брони.
         */
        protected int id;

        /**
         * Идентификатор пользователя, взявшего в аренду предмет.
         */
        protected int bookerId;
    }
}
