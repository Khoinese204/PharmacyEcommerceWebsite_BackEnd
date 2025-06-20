package com.example.pharmacywebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderStatusLog;

public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Integer> {

    List<OrderStatusLog> findByOrderOrderByUpdatedAtAsc(Order order);

    List<OrderStatusLog> findByOrderIdOrderByUpdatedAtAsc(Integer id);}