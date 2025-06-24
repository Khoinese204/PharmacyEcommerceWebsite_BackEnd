package com.example.pharmacywebsite.designpattern.FactoryMethod;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class BankTransferPaymentFactory implements PaymentFactory {
    @Override
    public PaymentTransaction createTransaction(Order order) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setPaymentMethod(order.getPaymentMethod());
        tx.setAmount(order.getTotalPrice());
        tx.setProviderTransactionId(UUID.randomUUID().toString());
        tx.setStatus(PaymentStatus.PENDING);
        tx.setPaidAt(LocalDateTime.now());
        tx.setCreatedAt(LocalDateTime.now());
        return tx;
    }
}