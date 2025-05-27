package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {}