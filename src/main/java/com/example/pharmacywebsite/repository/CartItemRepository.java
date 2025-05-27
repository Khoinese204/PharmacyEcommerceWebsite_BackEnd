package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}