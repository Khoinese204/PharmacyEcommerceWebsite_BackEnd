package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {
}
