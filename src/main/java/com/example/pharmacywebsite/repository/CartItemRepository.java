package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.CartItem;
import com.example.pharmacywebsite.domain.Cart;
import com.example.pharmacywebsite.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // Lấy tất cả cart item trong một cart
    List<CartItem> findByCartId(Integer cartId);

    // Kiểm tra xem cart đã có thuốc đó chưa
    Optional<CartItem> findByCartIdAndMedicineId(Integer cartId, Integer medicineId);

    // Lấy theo cart + medicine (nếu dùng kiểu đối tượng)
    Optional<CartItem> findByCartAndMedicine(Cart cart, Medicine medicine);

    // Xóa tất cả cart item trong một cart
    void deleteByCartId(Integer cartId);

    // Xóa một thuốc cụ thể khỏi cart
    void deleteByCartIdAndMedicineId(Integer cartId, Integer medicineId);

    // Đếm số item trong một cart
    long countByCartId(Integer cartId);
}
