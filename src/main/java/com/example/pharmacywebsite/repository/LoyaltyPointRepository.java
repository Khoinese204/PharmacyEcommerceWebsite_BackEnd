package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.LoyaltyPoint;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Integer> {}
