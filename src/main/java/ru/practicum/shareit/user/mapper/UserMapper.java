package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserAddUpdateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.model.User;

/**
 * Утилитарный класс для преобразования объектов типа User и связанных DTO.
 * Этот класс предоставляет методы для конвертации между сущностью User и DTO
 * {@link UserAddUpdateDto} и {@link UserGetDto}
 */
public class UserMapper {
    /**
     * Преобразует объект типа User в объект типа UserGetDto.
     *
     * @param user Объект типа User для преобразования
     * @return Объект типа UserGetDto
     */
    public static UserGetDto toGetUserDto(User user) {
        return UserGetDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * Преобразует объект типа User в объект типа UserAddUpdateDto.
     *
     * @param user Объект типа User для преобразования
     * @return Объект типа UserAddUpdateDto
     */
    public static UserAddUpdateDto toAddUpdateUserDto(User user) {
        return UserAddUpdateDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * Преобразует объект типа UserGetDto в объект типа User.
     *
     * @param userDto Объект типа UserGetDto для преобразования
     * @return Объект типа User
     */
    public static User toUser(UserGetDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    /**
     * Преобразует объект типа UserAddUpdateDto в объект типа User.
     *
     * @param userDto Объект типа UserAddUpdateDto для преобразования
     * @return Объект типа User
     */
    public static User toUser(UserAddUpdateDto userDto) {
        return User.builder()
                .id(-1)
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }
}
