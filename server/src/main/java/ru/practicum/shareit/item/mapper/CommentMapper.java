package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

/**
 * Класс-маппер для преобразования между объектами Comment и CommentDto.
 */
public class CommentMapper {

    /**
     * Преобразует объект Comment в CommentDto.
     *
     * @param comment объект Comment, который нужно преобразовать.
     * @return объект CommentDto, представляющий комментарий.
     */
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
    }

    /**
     * Преобразует объект CommentDto в Comment.
     *
     * @param commentDto объект CommentDto, который нужно преобразовать.
     * @return объект Comment, представляющий комментарий.
     */
    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .created(commentDto.getCreated())
                .text(commentDto.getText())
                .build();
    }
}
