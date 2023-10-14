package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping
    public Collection<ItemRequestDto> findAllByUserId(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findAll(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "5") @Positive Integer size) {
        return itemRequestService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                   @PathVariable Integer requestId) {
        return itemRequestService.findById(userId, requestId);
    }

    @PostMapping
    public ItemRequestDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(itemRequestService.addItem(userId, itemRequest));
    }

//    @GetMapping
//    public Collection<ItemRequestDto> findAllByAnyone() {
//        return itemRequestService.findAllByAnyone().stream()
//                .map(ItemRequestMapper::toItemRequestDto)
//                .collect(Collectors.toList());
//    }
}
