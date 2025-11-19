package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.dto.BestSellingProductDto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    List<Medicine> findByNameContainingIgnoreCase(String keyword);

    @Query(value = """
                SELECT
                    m.id,
                    m.name,
                    m.image_url,
                    m.original_price,
                    m.price,
                    m.unit
                FROM order_items oi
                JOIN medicines m ON oi.medicine_id = m.id
                GROUP BY m.id, m.name, m.image_url, m.original_price, m.price, m.unit
                ORDER BY SUM(oi.quantity) DESC
                LIMIT 8
            """, nativeQuery = true)
    List<Object[]> findTop8BestSellingRaw(); // Dạng raw dữ liệu

    @Query("SELECT m.imageUrl FROM Medicine m WHERE m.imageUrl LIKE 'product%.jpg'")
    List<String> findAllProductImageNames();
    List<Medicine> findByCategory_Slug(String slug);
}
