package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.ImportOrder;

public interface ImportOrderRepository extends JpaRepository<ImportOrder, Integer> {}