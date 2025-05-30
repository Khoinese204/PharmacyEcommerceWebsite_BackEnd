package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Cart;
import com.example.pharmacywebsite.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    // Tìm giỏ hàng theo ID người dùng
    Optional<Cart> findByUserId(Integer userId);

    // Kiểm tra giỏ hàng đã tồn tại cho người dùng chưa
    boolean existsByUserId(Integer userId);

    // Xóa giỏ hàng theo người dùng (nếu cần dùng cho logout hoặc reset)
    void deleteByUserId(Integer userId);
}
