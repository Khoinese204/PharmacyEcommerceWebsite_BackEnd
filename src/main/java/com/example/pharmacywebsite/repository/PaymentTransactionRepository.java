package com.example.pharmacywebsite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    List<PaymentTransaction> findByOrder_User_Id(Integer userId);

    PaymentTransaction findByOrder(Order order);

    Optional<PaymentTransaction> findByOrder_Id(Integer orderId);
}
