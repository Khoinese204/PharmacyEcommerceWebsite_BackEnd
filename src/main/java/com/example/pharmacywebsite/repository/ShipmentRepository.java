package com.example.pharmacywebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {}