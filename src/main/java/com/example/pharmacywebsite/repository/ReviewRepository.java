package com.example.pharmacywebsite.repository;

import com.example.pharmacywebsite.domain.Review;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @EntityGraph(attributePaths = { "comments", "comments.user", "user", "medicine" })
    @Query("SELECT r FROM Review r WHERE r.medicine.id = :medicineId")
    List<Review> findByMedicineIdFetchAll(@Param("medicineId") Integer medicineId);

    @EntityGraph(attributePaths = { "comments", "comments.user", "user", "medicine" })
    @Query("SELECT r FROM Review r WHERE r.medicine.id = :medicineId AND r.rating = :rating")
    List<Review> findByMedicineIdAndRatingFetchAll(@Param("medicineId") Integer medicineId,
            @Param("rating") Integer rating);

    List<Review> findByMedicineId(Integer medicineId);
}
