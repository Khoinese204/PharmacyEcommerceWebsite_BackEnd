package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.WarehouseDeliveryLog;

public interface WarehouseDeliveryLogRepository extends JpaRepository<WarehouseDeliveryLog, Integer> {
}
