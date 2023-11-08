package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    User user;

    Item item;

    PageRequest pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Test")
                .email("Test@test.test")
                .build();
        em.persist(user);

        item = Item.builder()
                .owner(user)
                .name("Item name")
                .available(true)
                .description("Desc word")
                .build();
        em.persist(item);
    }

    @Test
    void testSearch_butZeroFound() {
        Page<Item> resultPage = itemRepository.search("Zero", pageable);

        assertEquals(0, resultPage.getContent().size());
    }

    @Test
    void testSearch_oneFoundByName() {
        Page<Item> resultPage = itemRepository.search("Item", pageable);

        assertEquals(1, resultPage.getContent().size());
        assertEquals(item, resultPage.getContent().get(0));
    }

    @Test
    void testSearch_oneFoundByDescription() {
        Page<Item> resultPage = itemRepository.search("word", pageable);

        assertEquals(1, resultPage.getContent().size());
        assertEquals(item, resultPage.getContent().get(0));
    }

    @Test
    void testSearch_twoFound() {
        Item item2 = Item.builder()
                .owner(user)
                .name("Item name")
                .available(true)
                .description("Desc2 word")
                .build();
        em.persist(item2);

        Page<Item> resultPage = itemRepository.search("word", pageable);

        assertEquals(2, resultPage.getContent().size());
        assertEquals(item, resultPage.getContent().get(0));
        assertEquals(item2, resultPage.getContent().get(1));
    }
}