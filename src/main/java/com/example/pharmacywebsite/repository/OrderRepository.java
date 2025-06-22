package com.example.pharmacywebsite.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pharmacywebsite.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);

    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);

    // ✅ CÁI NÀY PHẢI CÓ (đúng cú pháp Spring Data)
    List<Order> findByUser_IdOrderByOrderDateDesc(Integer userId);

    // Tổng doanh thu trong khoảng thời gian
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    Double sumTotalRevenueByMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Doanh thu theo từng ngày (dùng cho biểu đồ)
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    Double sumRevenueByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Đếm số đơn hàng theo thời gian
    Long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}
