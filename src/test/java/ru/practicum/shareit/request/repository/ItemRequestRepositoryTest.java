package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User user;
    ItemRequest itemRequest;

    PageRequest pageable = PageRequest.of(0, 10);

    @Transactional
    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("Test")
                .email("Test@test.test")
                .build();
        em.persist(user);

        itemRequest = ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("Text text")
                .requestor(user)
                .build();

        em.persist(itemRequest);
    }

    @Transactional
    @Test
    void testFindRequestsCreatedByOthersOrderByCreated_ifOnlyOneRequest() {
        Page<ItemRequest> resultPage = itemRequestRepository.findRequestsCreatedByOthersOrderByCreated(user, pageable);

        assertEquals(0, resultPage.getContent().size());
    }

    @Transactional
    @Test
    void testFindRequestsCreatedByOthersOrderByCreated_ifOtherUserAddRequest() {
        User user2 = User.builder()
                .name("Test2")
                .email("Test2@test.test")
                .build();
        em.persist(user2);

        ItemRequest itemRequest2 = ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("Test trest")
                .requestor(user2)
                .build();

        em.persist(itemRequest2);
        Page<ItemRequest> resultPage = itemRequestRepository.findRequestsCreatedByOthersOrderByCreated(user, pageable);

        assertEquals(1, resultPage.getContent().size());
        assertEquals("Test trest", resultPage.getContent().get(0).getDescription());
        assertEquals(user2, resultPage.getContent().get(0).getRequestor());
    }

    @Transactional
    @Test
    void testFindRequestsCreatedByOthersOrderByCreated_IfOtherUserAddRequest_AndCurrentAddRequest() {
        User user2 = User.builder()
                .name("Test2")
                .email("Test2@test.test")
                .build();
        em.persist(user2);

        ItemRequest itemRequest2 = ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("Test req1")
                .requestor(user2)
                .build();

        ItemRequest itemRequest3 = ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("Test req2")
                .requestor(user)
                .build();

        em.persist(itemRequest2);
        em.persist(itemRequest3);

        Page<ItemRequest> resultPage = itemRequestRepository.findRequestsCreatedByOthersOrderByCreated(user, pageable);

        assertEquals(1, resultPage.getContent().size());
        assertEquals("Test req1", resultPage.getContent().get(0).getDescription());
        assertEquals(user2, resultPage.getContent().get(0).getRequestor());
    }
}