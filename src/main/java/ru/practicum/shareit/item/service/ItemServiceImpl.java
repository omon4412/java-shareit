package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.WrongOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDao;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDao;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemStorage;
    private final UserDao userStorage;

    @Override
    public Item addItem(Item item, int ownerID) {
        User owner = userStorage.get(ownerID);
        if (owner == null) {
            log.error("Пользователь с id=" + ownerID + " не найден");
            throw new UserNotFoundException("Пользователь с id=" + ownerID + " не найден");
        }
        item.setOwner(owner);
        return itemStorage.save(item);
    }

    @Override
    public Item getItem(int itemId) {
        Item item = itemStorage.get(itemId);
        if (item == null) {
            log.error("Вещь с id=" + itemId + " не найдена");
            throw new ItemNotFoundException("Вещь с id=" + itemId + " не найдена");
        }
        return item;
    }

    @Override
    public Item updateItem(Item item, int userId) {
        int itemId = item.getId();
        Item checkItem = itemStorage.get(itemId);
        if (checkItem == null) {
            log.error("Вещь с id=" + itemId + " не найдена");
            throw new ItemNotFoundException("Вещь с id=" + itemId + " не найдена");
        }

        User owner = userStorage.get(userId);
        if (owner == null) {
            log.error("Пользователь с id=" + userId + " не найден");
            throw new UserNotFoundException("Пользователь с id=" + userId + " не найден");
        }

        if (!checkItem.getOwner().equals(owner)) {
            throw new WrongOwnerException("");
        }

        return itemStorage.update(item);
    }

    @Override
    public Item deleteItem(int itemId) {
        Item item = itemStorage.delete(itemId);
        if (item == null) {
            log.error("Вещь с id=" + itemId + " не найдена");
            throw new ItemNotFoundException("Вещь с id=" + itemId + " не найдена");
        }
        return item;
    }

    @Override
    public Collection<Item> getAll(int userId) {
        User owner = userStorage.get(userId);
        if (owner == null) {
            log.error("Пользователь с id=" + userId + " не найден");
            throw new UserNotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return itemStorage.getAll(userId);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) {
            log.debug("Пустой запрос");
            return Collections.emptyList();
        }
        return itemStorage.searchItems(text);
    }
}
