package com.example.pharmacywebsite.designpattern.FactoryMethod;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.enums.PaymentStatus;

import java.time.LocalDateTime;

public class CodPaymentFactory implements PaymentFactory {
    @Override
    public PaymentTransaction createTransaction(Order order) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setPaymentMethod(order.getPaymentMethod());
        tx.setAmount(order.getTotalPrice());
        tx.setStatus(PaymentStatus.PENDING);
        tx.setCreatedAt(LocalDateTime.now());
        return tx;
    }
}
