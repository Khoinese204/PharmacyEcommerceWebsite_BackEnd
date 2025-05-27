package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
