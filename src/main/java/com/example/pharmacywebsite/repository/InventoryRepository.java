package com.example.pharmacywebsite.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.pharmacywebsite.domain.Inventory;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.enums.InventoryStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByMedicineAndStatusOrderByExpiredAtAsc(Medicine medicine, InventoryStatus status);

    List<Inventory> findByMedicineOrderByExpiredAtAsc(Medicine medicine);

    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.medicine.id = :medicineId AND i.status = 'ACTIVE' AND i.expiredAt >= :today")
    Integer sumQuantityByMedicineIdAndNotExpired(
            @Param("medicineId") Integer medicineId,
            @Param("today") LocalDate today);

    Long countByQuantityLessThanEqual(Integer threshold);

    // ✅ Số sản phẩm tồn kho thấp được tạo trong ngày (dùng cho biểu đồ)
    Long countByCreatedAtBetweenAndQuantityLessThanEqual(LocalDateTime start, LocalDateTime end, Integer threshold);

    List<Inventory> findByQuantityLessThanEqual(Integer threshold);

    Optional<Inventory> findByMedicineId(Long medicineId);

    List<Inventory> findByMedicineId(Integer medicineId);

    List<Inventory> findByMedicineIdIn(List<Integer> ids);

    List<Inventory> findAllByMedicineId(Long medicineId);

    @Query("SELECT COUNT(DISTINCT i.medicine.id) FROM Inventory i")
    long countDistinctMedicineTypes();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantity <= 20")
    long countLowStock(); // hoặc tuỳ bạn chọn ngưỡng

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.expiredAt < CURRENT_DATE")
    long countExpired();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.expiredAt BETWEEN :today AND :next30Days")
    long countNearExpiry(@Param("today") LocalDate today, @Param("next30Days") LocalDate next30Days);

}
