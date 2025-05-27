package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
