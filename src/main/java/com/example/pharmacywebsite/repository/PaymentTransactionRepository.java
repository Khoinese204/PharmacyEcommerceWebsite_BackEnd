package com.example.pharmacywebsite.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.enums.PaymentStatus;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    List<PaymentTransaction> findByOrder_User_Id(Integer userId);

    PaymentTransaction findByOrder(Order order);

    Optional<PaymentTransaction> findByOrder_Id(Integer orderId);

    Optional<PaymentTransaction> findByProviderTransactionId(String providerTransactionId);

    @Query("SELECT SUM(p.amount) FROM PaymentTransaction p WHERE p.createdAt BETWEEN :start AND :end AND p.status = :status")
    Double sumAmountByCreatedAtBetweenAndStatus(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") PaymentStatus status);
}
