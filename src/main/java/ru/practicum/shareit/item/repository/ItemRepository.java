package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> getAllByOwnerId(int ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE lower(i.name) LIKE lower(concat('%', ?1, '%')) " +
            "OR lower(i.description) LIKE lower(concat('%', ?1, '%')) " +
            "AND i.available = true")
    List<Item> search(String text);
}
