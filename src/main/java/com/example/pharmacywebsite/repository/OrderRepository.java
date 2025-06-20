package com.example.pharmacywebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);

    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);

    // ✅ CÁI NÀY PHẢI CÓ (đúng cú pháp Spring Data)
    List<Order> findByUser_IdOrderByOrderDateDesc(Integer userId);
}
