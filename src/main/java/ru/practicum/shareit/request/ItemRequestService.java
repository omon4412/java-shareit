package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    Collection<ItemRequestDto> findAllByUserId(Integer userId);

    Collection<ItemRequestDto> findAll(Integer userId, int from, int size);


    ItemRequestDto findById(Integer userId, Integer requestId);


    ItemRequest addItem(Integer userId, ItemRequest itemRequest);
}
