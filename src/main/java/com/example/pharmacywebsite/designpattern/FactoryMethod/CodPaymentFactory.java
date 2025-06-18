package com.example.pharmacywebsite.designpattern.FactoryMethod;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("COD")
public class CodPaymentFactory implements PaymentFactory {

    @Override
    public PaymentTransaction createTransaction(Order order, Double amount) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setAmount(amount);
        tx.setPaymentMethod(PaymentMethod.COD);
        tx.setStatus(PaymentStatus.PENDING);
        tx.setCreatedAt(LocalDateTime.now());
        return tx;
    }
}
