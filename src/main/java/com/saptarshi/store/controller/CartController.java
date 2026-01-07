package com.saptarshi.store.controller;

import com.saptarshi.store.dto.AddItemToCartRequest;
import com.saptarshi.store.dto.CartDto;
import com.saptarshi.store.entities.Cart;
import com.saptarshi.store.entities.CartItem;
import com.saptarshi.store.mappers.CartMapper;
import com.saptarshi.store.repositories.CartRepository;
import com.saptarshi.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items") // 1. Added Path
    public ResponseEntity<CartDto> addToCart(@PathVariable UUID cartId,
                                             @RequestBody AddItemToCartRequest request
    ) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build(); // 2. Changed to Not Found

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().build(); // 3. Changed to Bad Request

        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);

        // 5. Return the DTO
        var cartItemDto = cartMapper.toDto(cartItem.getCart());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
}
