package com.example.pharmacywebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.enums.InventoryStatus;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByMedicineAndStatusOrderByExpiredAtAsc(Medicine medicine, InventoryStatus status);

}