package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.VouchersApplied;

public interface VouchersAppliedRepository extends JpaRepository<VouchersApplied, Integer> {
}