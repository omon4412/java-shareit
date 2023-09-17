package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserAddUpdateDto;
import ru.practicum.shareit.user.dto.UserGetDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    /**
     * Сервис управления пользователями.
     */
    private final UserService userService;

    /**
     * Добавление нового пользователя.
     *
     * @param userDto Данные нового пользователя
     * @return Данные добавленного пользователя
     */
    @PostMapping
    public UserGetDto addUser(@Valid @RequestBody UserAddUpdateDto userDto) {
        log.debug("Начато добавление пользователя - " + userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toGetUserDto(userService.addUser(user));
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{userId}")
    public UserGetDto getUser(@PathVariable @PositiveOrZero int userId) {
        log.debug("Начат поиск пользователя - " + userId);
        return UserMapper.toGetUserDto(userService.getUser(userId));
    }

    /**
     * Обновление данных пользователя.
     *
     * @param userId  Идентификатор пользователя, данные которого нужно обновить
     * @param userDto Новые данные пользователя
     * @return Обновленные данные пользователя
     */
    @PatchMapping("/{userId}")
    public UserGetDto updateUser(@PathVariable @PositiveOrZero int userId,
                                 @RequestBody UserAddUpdateDto userDto) {
        log.debug("Начато обновление пользователя - " + userDto);
        User user = UserMapper.toUser(userDto);
        user.setId(userId);
        return UserMapper.toGetUserDto(userService.updateUser(user));
    }

    /**
     * Удаление пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя, которого нужно удалить
     * @return Данные удаленного пользователя
     */
    @DeleteMapping("/{userId}")
    public UserGetDto deleteUser(@PathVariable @PositiveOrZero int userId) {
        log.debug("Начато удаление пользователя - " + userId);
        return UserMapper.toGetUserDto(userService.deleteUser(userId));
    }

    /**
     * Получение списка всех пользователей.
     *
     * @return Список пользователей
     */
    @GetMapping
    public Collection<UserGetDto> getAll() {
        Collection<UserGetDto> users = userService.getAll().stream()
                .map(UserMapper::toGetUserDto)
                .collect(Collectors.toList());
        log.debug("Количество пользователей - " + users.size());
        return users;
    }
}
