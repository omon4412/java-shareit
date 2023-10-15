package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    Collection<ItemRequest> findAllByRequestorIdOrderByCreated(int requestorId);

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requestor <> :currentUser")
    Page<ItemRequest> findRequestsCreatedByOthersOrderByCreated(@Param("currentUser") User currentUser, Pageable pageable);
}
