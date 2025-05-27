package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.ImportOrderItem;

public interface ImportOrderItemRepository extends JpaRepository<ImportOrderItem, Integer> {}