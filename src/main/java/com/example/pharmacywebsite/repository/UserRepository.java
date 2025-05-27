package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
