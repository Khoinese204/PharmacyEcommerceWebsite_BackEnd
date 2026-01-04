package com.example.pharmacywebsite.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE oi.medicine.id = :medicineId " +
            "AND o.orderDate >= :startDate " +
            "AND o.status = 'DELIVERED'") 
    Integer countSoldMedicineSince(@Param("medicineId") Integer medicineId,
            @Param("startDate") LocalDateTime startDate);

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByOrderId(Integer orderId);
}
