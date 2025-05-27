package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
