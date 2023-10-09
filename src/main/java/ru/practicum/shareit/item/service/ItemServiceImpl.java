package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.WrongOwnerException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemStorage;
    private final UserRepository userStorage;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(int itemId, Integer userId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Предмет с id=" + itemId + " не найден");
                    return new ItemNotFoundException("Предмет с id=" + itemId + " не найден");
                });
        ItemDto itemDto = ItemMapper.toItemDto(item);

        if (item.getOwner().getId() == userId) {
            List<Booking> lastBooking = bookingRepository.findLastBooking(userId, itemId,
                    LocalDateTime.now(), BookingStatus.APPROVED);
            itemDto.setLastBooking(lastBooking.size() == 0 ? null
                    : new ItemDto.BookingInfo(lastBooking.get(0).getId(), lastBooking.get(0).getBooker().getId()));

            List<Booking> nextBooking = bookingRepository.findNextBooking(userId, itemId,
                    LocalDateTime.now(), BookingStatus.APPROVED);
            itemDto.setNextBooking(nextBooking.size() == 0 ? null
                    : new ItemDto.BookingInfo(nextBooking.get(0).getId(), nextBooking.get(0).getBooker().getId()));
        }
        itemDto.setComments(commentRepository.findAllByItemIdOrderByIdDesc(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

        return itemDto;
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

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDto> getAll(int userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        List<Item> items = itemStorage.getAllByOwnerIdOrderByOwnerId(userId);
        items.sort(Comparator.comparing(Item::getId));

        return items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.toItemDto(item);
                    itemDto.setComments(commentRepository.findAllByItemIdOrderByIdDesc(itemDto.getId())
                            .stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList()));

                    if (item.getOwner().getId() == userId) {
                        List<Booking> lastBooking = bookingRepository.findLastBooking(userId, itemDto.getId(),
                                LocalDateTime.now(), BookingStatus.APPROVED);
                        itemDto.setLastBooking(lastBooking.size() == 0 ? null
                                : new ItemDto.BookingInfo(lastBooking.get(0).getId(),
                                lastBooking.get(0).getBooker().getId()));

                        List<Booking> nextBooking = bookingRepository.findNextBooking(userId, itemDto.getId(),
                                LocalDateTime.now(), BookingStatus.APPROVED);
                        itemDto.setNextBooking(nextBooking.size() == 0 ? null
                                : new ItemDto.BookingInfo(nextBooking.get(0).getId(),
                                nextBooking.get(0).getBooker().getId()));
                    }
                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) {
            log.debug("Пустой запрос");
            return Collections.emptyList();
        }
        return itemStorage.search(text);
    }

    @Transactional
    @Override
    public Comment addComment(Integer userId, int itemId, Comment comment) {
        User author = userStorage.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id=" + userId + " не найден");
                    return new UserNotFoundException("Пользователь с id=" + userId + " не найден");
                });

        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Предмет с id=" + itemId + " не найден");
                    return new ItemNotFoundException("Предмет с id=" + itemId + " не найден");
                });

        if (bookingRepository.findByBookerIdAndItemIdAndStatusIsAndEndBefore(
                userId, itemId, BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            log.error("Пользователь с ID=" + userId
                    + " не брал предмет в аренду или срок завершения аренды не наступил");
            throw new BookingBadRequestException("Пользователь с ID=" + userId
                    + " не брал предмет в аренду или срок завершения аренды не наступил");
        }

        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
