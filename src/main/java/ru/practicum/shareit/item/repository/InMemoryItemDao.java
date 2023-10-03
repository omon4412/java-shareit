package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryItemDao implements ItemDao {

    private int lastId = 0;

    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        int itemId = generateId();
        item.setId(itemId);
        items.put(itemId, item);
        log.debug("Вещь - " + item + "добавлена");
        return item;
    }

    @Override
    public Item update(Item item) {
        Item oldItem = items.get(item.getId());
        String name = item.getName();
        if (name != null) {
            oldItem.setName(name);
        }

        String description = item.getDescription();
        if (description != null) {
            oldItem.setDescription(description);
        }

        Boolean available = item.getAvailable();
        if (available != null) {
            oldItem.setAvailable(available);
        }
        log.debug("Вещь - " + oldItem + "обнавлена");
        return oldItem;
    }

    @Override
    public Item get(int itemId) {
        Item item = items.get(itemId);
        log.debug("Вещь - " + item + "получена");
        return item;
    }

    @Override
    public Item delete(int itemId) {
        Item item = items.get(itemId);
        items.remove(itemId);
        log.debug("Вещь - " + item + "удалена");
        return item;
    }

    @Override
    public Collection<Item> getAll(int userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }

    private int generateId() {
        return ++lastId;
    }
}
