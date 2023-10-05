package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.WrongOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemStorage;
    private final UserRepository userStorage;

    @Override
    public Item addItem(Item item, int ownerID) {
        User owner = userStorage.findById(ownerID)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + ownerID + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + ownerID + " не найден");
                });
        item.setOwner(owner);
        return itemStorage.save(item);
    }

    @Override
    public Item getItem(int itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Предмет с id=" + itemId + " не найден");
                    return new ItemNotFoundException("Предмет с id=" + itemId + " не найден");
                });
        return item;
    }

    @Override
    @Transactional
    public Item updateItem(Item item, int userId) {
        int itemId = item.getId();
        Item checkItem = itemStorage.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Предмет с id=" + itemId + " не найден");
                    return new ItemNotFoundException("Предмет с id=" + itemId + " не найден");
                });

        User owner = userStorage.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        if (!checkItem.getOwner().equals(owner)) {
            throw new WrongOwnerException("Доступ запрещён");
        }
        //item.setOwner(checkItem.getOwner());

        if (item.getName() != null && !item.getName().isBlank()) {
            checkItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            checkItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            checkItem.setAvailable(item.getAvailable());
        }

        return itemStorage.save(checkItem);
    }

    @Override
    public Item deleteItem(int itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Предмет с id=" + itemId + " не найден");
                    return new ItemNotFoundException("Предмет с id=" + itemId + " не найден");
                });

        itemStorage.delete(item);
        return item;
    }

    @Override
    public Collection<Item> getAll(int userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });
        return itemStorage.getAllByOwnerId(userId);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) {
            log.debug("Пустой запрос");
            return Collections.emptyList();
        }
        return itemStorage.search(text);
    }
}
