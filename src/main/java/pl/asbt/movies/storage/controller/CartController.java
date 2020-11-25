package pl.asbt.movies.storage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.asbt.movies.storage.dto.CartDto;
import pl.asbt.movies.storage.dto.OrderDto;
import pl.asbt.movies.storage.exception.ErrorType;
import pl.asbt.movies.storage.exception.StorageException;
import pl.asbt.movies.storage.mapper.CartMapper;
import pl.asbt.movies.storage.mapper.OrderMapper;
import pl.asbt.movies.storage.servise.CartService;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/storage/cards")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void createCart(@Validated @RequestBody CartDto cartDto) {
        cartService.saveCart(cartMapper.matToCart(cartDto));
    }

    @GetMapping(value = "/{cardId}")
    public CartDto getCard(@Validated @PathVariable Long cardId) throws StorageException {
        return cartMapper.mapToCartDto(cartService.getCart(cardId).orElseThrow(() ->
                StorageException.builder()
                        .errorType(ErrorType.NOT_FOUND)
                        .message("There are no cart with given id.")
                        .build()
        ));
    }

    @GetMapping
    public List<CartDto> getCards() {
        return cartMapper.mapToCartsDto(cartService.getAllCarts());
    }

    @DeleteMapping(value = "/{cardId}")
    public void deleteCard(@Validated @PathVariable Long cardId) {
        cartService.deleteCart(cardId);
    }

    @PatchMapping(value = "/{cartId}/{userId}")
    public OrderDto createOrder(@Validated Long cartId, @Validated Long userId) throws StorageException {
        return orderMapper.mapToOrderDto(cartService.createOrder(cartId, userId));
    }

    // To:Do Add-Item
    // To:DO sub-Item

}
