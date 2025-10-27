package com.example.pharmacywebsite.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.enums.OrderStatus;
import com.example.pharmacywebsite.enums.PaymentStatus;
import com.example.pharmacywebsite.repository.OrderRepository;
import com.example.pharmacywebsite.repository.PaymentTransactionRepository;

@Service
public class PaymentTransactionService {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void updatePaymentStatus(String txnRef, String vnpResponseCode) {
        PaymentTransaction transaction = paymentTransactionRepository
                .findByProviderTransactionId(txnRef)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch với mã: " + txnRef));

        if ("00".equals(vnpResponseCode)) {
            transaction.setStatus(PaymentStatus.SUCCESS);
            transaction.setPaidAt(LocalDateTime.now());

            Order order = transaction.getOrder();
            order.setStatus(OrderStatus.PENDING);

            orderRepository.save(order);
        } else {
            transaction.setStatus(PaymentStatus.PENDING);
        }

        paymentTransactionRepository.save(transaction);
    }
}
