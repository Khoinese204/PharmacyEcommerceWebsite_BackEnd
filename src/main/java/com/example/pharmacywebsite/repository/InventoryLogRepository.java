package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.InventoryLog;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Integer> {}