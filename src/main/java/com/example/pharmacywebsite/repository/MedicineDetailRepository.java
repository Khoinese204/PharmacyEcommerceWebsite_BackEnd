package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.MedicineDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineDetailRepository extends JpaRepository<MedicineDetail, Integer> {
}
