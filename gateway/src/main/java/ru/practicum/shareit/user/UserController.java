package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.PositiveOrZero;

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
    private final UserClient userClient;

    /**
     * Добавление нового пользователя.
     *
     * @param userDto Данные нового пользователя
     * @return Данные добавленного пользователя
     */
    @PostMapping
    public ResponseEntity<Object> addUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.debug("Начато добавление пользователя - " + userDto);
        return userClient.createUser(userDto);
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя
     * @return Данные пользователя
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable @PositiveOrZero int userId) {
        log.debug("Начат поиск пользователя - " + userId);
        return userClient.getUser(userId);
    }

    /**
     * Обновление данных пользователя.
     *
     * @param userId  Идентификатор пользователя, данные которого нужно обновить
     * @param userDto Новые данные пользователя
     * @return Обновленные данные пользователя
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @PositiveOrZero int userId,
                                             @Validated(Update.class) @RequestBody UserDto userDto) {
        log.debug("Начато обновление пользователя - " + userDto);
        return userClient.updateUser(userId, userDto);
    }

    /**
     * Удаление пользователя по идентификатору.
     *
     * @param userId Идентификатор пользователя, которого нужно удалить
     * @return Данные удаленного пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable @PositiveOrZero int userId) {
        log.debug("Начато удаление пользователя - " + userId);
        return userClient.deleteUser(userId);
    }

    /**
     * Получение списка всех пользователей.
     *
     * @return Список пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getUsers();
    }
}
