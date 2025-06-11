package com.example.pharmacywebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder(Order order);
}
