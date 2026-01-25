package com.saptarshi.store.controller;

import com.saptarshi.store.dto.CheckOutRequest;
import com.saptarshi.store.dto.CheckOutResponse;
import com.saptarshi.store.entities.Order;
import com.saptarshi.store.entities.OrderItem;
import com.saptarshi.store.entities.OrderStatus;
import com.saptarshi.store.repositories.CartRepository;
import com.saptarshi.store.service.AuthService;
import com.saptarshi.store.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckOutController {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckOutRequest request
    ) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "cart not found")
            );
        }

        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "cart is empty")
            );
        }

        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });

        orderRepository.save(order);
        cartService.clearCart(cart.getId());


        return ResponseEntity.ok(new CheckOutResponse(order.getId()));
    }
}
