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
    public CartDto getCart(@PathVariable(name = "userId") Integer userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/add")
    public void addToCart(@PathVariable(name = "userId") Integer userId, @RequestBody AddToCartRequest request) {
        cartService.addToCart(userId, request);
    }

    @PutMapping("/{userId}/item/{itemId}")
    public void updateCartItem(@PathVariable(name = "userId") Integer userId,
            @PathVariable(name = "itemId") Integer itemId,
            @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItem(userId, itemId, request);
    }

    @DeleteMapping("/{userId}/item/{itemId}")
    public void removeItem(@PathVariable(name = "userId") Integer userId,
            @PathVariable(name = "itemId") Integer itemId) {
        cartService.removeCartItem(userId, itemId);
    }

    // Xóa tất cả sản phẩm trong giỏ hàng của người dùng
    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable(name = "userId") Integer userId) {
        cartService.clearCart(userId);
    }
}
