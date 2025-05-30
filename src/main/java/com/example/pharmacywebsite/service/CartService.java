package com.example.pharmacywebsite.service;

import com.example.pharmacywebsite.domain.*;
import com.example.pharmacywebsite.dto.*;
import com.example.pharmacywebsite.exception.ApiException;
import com.example.pharmacywebsite.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private MedicineRepository medicineRepo;
    @Autowired
    private UserRepository userRepo;

    public CartDto getCartByUserId(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        Cart cart = cartRepo.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(userRepo.findById(userId).orElseThrow());
            newCart.setCreatedAt(LocalDateTime.now());
            newCart.setUpdatedAt(LocalDateTime.now());
            return cartRepo.save(newCart);
        });

        List<CartItemDto> items = cart.getItems().stream().map(item -> {
            CartItemDto dto = new CartItemDto();
            dto.setId(item.getId());
            dto.setMedicineId(item.getMedicine().getId());
            dto.setMedicineName(item.getMedicine().getName());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).toList();

        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(userId);
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        dto.setItems(items);
        return dto;
    }

    @Transactional
    public void addToCart(Integer userId, AddToCartRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        Cart cart = cartRepo.findByUserId(userId).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(userRepo.findById(userId).orElseThrow());
            c.setCreatedAt(LocalDateTime.now());
            c.setUpdatedAt(LocalDateTime.now());
            return cartRepo.save(c);
        });

        CartItem item = cartItemRepo.findByCartIdAndMedicineId(cart.getId(), req.getMedicineId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setMedicine(medicineRepo.findById(req.getMedicineId()).orElseThrow());
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + req.getQuantity());
        cartItemRepo.save(item);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepo.save(cart);
    }

    public void updateCartItem(Integer userId, Integer itemId, UpdateCartItemRequest req) {
        // Bước 1: Kiểm tra người dùng
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        // Bước 2: Tìm CartItem
        CartItem item = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new ApiException("Cart item not found", HttpStatus.NOT_FOUND));

        // Bước 4: Cập nhật số lượng
        item.setQuantity(req.getQuantity());
        cartItemRepo.save(item);

        // Bước 5: Cập nhật thời gian sửa giỏ hàng
        Cart cart = item.getCart();
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepo.save(cart);
    }

    public void removeCartItem(Integer userId, Integer itemId) {
        // Kiểm tra user tồn tại
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        // Tìm cart item
        CartItem item = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new ApiException("Cart item not found", HttpStatus.NOT_FOUND));
        // Xóa cart item
        cartItemRepo.deleteById(itemId);

        // Cập nhật lại thời gian của cart
        Cart cart = item.getCart();
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepo.save(cart);
    }

    public void clearCart(Integer userId) {
        // Kiểm tra user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        // Tìm giỏ hàng
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new ApiException("Cart not found", HttpStatus.NOT_FOUND));

        // Xóa toàn bộ items trong cart
        cartItemRepo.deleteByCartId(cart.getId());

        // Cập nhật lại thời gian
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepo.save(cart);
    }
}
