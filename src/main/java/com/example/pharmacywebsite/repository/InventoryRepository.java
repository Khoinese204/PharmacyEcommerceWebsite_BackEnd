package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {}