package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
