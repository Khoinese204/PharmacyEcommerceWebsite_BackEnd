package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email); // <--- THÊM

    Optional<User> findByEmail(String email); // <--- THÊM
}
