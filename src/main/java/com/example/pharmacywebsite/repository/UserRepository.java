package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email); // <--- THÊM

    Optional<User> findByEmail(String email); // <--- THÊM

    Long countByRole_Name(String roleName); // ✅ ĐÚNG
    // ✅ Số user mới đăng ký theo ngày (dùng cho biểu đồ khách hàng)

    Long countByCreatedAtBetweenAndRole_Name(LocalDateTime start, LocalDateTime end, String roleName);

    List<User> findByRole_Name(String roleName);

}
