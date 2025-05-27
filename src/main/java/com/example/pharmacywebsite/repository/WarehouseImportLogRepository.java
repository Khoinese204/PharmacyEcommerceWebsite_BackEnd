package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.WarehouseImportLog;

public interface WarehouseImportLogRepository extends JpaRepository<WarehouseImportLog, Integer> {}