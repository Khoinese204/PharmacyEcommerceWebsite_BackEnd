package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name); // <--- THÊM
}
