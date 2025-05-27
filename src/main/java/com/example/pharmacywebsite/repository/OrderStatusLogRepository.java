package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.OrderStatusLog;

public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Integer> {}