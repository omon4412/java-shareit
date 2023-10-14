package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> getAllByOwnerIdOrderByOwnerId(int ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i " +
            "WHERE lower(i.name) LIKE lower(concat('%', ?1, '%')) " +
            "OR lower(i.description) LIKE lower(concat('%', ?1, '%')) " +
            "AND i.available = true")
    Page<Item> search(String text, Pageable pageable);

    List<Item> findAllByRequestId(int requestId);
}
