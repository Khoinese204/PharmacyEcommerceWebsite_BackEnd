package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.*;
import com.example.pharmacywebsite.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public CartDto getCart(@PathVariable Integer userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/add")
    public void addToCart(@PathVariable Integer userId, @RequestBody AddToCartRequest request) {
        cartService.addToCart(userId, request);
    }

    @PutMapping("/item/{itemId}")
    public void updateCartItem(@PathVariable Integer itemId, @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItem(itemId, request);
    }

    @DeleteMapping("/item/{itemId}")
    public void removeItem(@PathVariable Integer itemId) {
        cartService.removeCartItem(itemId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
    }
}
