package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    List<Medicine> findByNameContainingIgnoreCase(String keyword);
}
