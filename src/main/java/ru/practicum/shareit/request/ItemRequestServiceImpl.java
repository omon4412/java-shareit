package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemRequestDto> findAllByUserId(Integer userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });
        Collection<ItemRequestDto> itemRequests = itemRequestRepository
                .findAllByRequestorIdOrderByCreated(requester.getId()).stream()
                .map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
        itemRequests.forEach(ir -> ir.setItems(itemRepository.findAllByRequestId(ir.getId()).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList())));
        return itemRequests;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemRequestDto> findAll(Integer userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        PageRequest pageRequest = PageRequest.of(from / size, size);

        Collection<ItemRequestDto> itemRequests = itemRequestRepository
                .findRequestsCreatedByOthersOrderByCreated(user, pageRequest)
                .getContent().stream()
                .map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
        itemRequests.forEach(ir -> ir.setItems(itemRepository.findAllByRequestId(ir.getId()).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList())));
        return itemRequests;
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto findById(Integer userId, Integer requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Запрос с id=" + requestId + " не найден");
                    return new UserNotFoundException("Запрос с id=" + requestId + " не найден");
                });

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request);
        itemRequestDto.setItems(itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList()));

        return itemRequestDto;
    }

    @Transactional
    @Override
    public ItemRequest addItem(Integer userId, ItemRequest itemRequest) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });
        itemRequest.setRequestor(requester);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(itemRequest);
    }
}
