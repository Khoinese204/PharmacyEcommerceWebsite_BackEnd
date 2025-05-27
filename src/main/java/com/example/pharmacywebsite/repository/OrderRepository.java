package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
