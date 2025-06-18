package com.example.pharmacywebsite.designpattern.FactoryMethod;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("BANK_TRANSFER")
public class BankTransferPaymentFactory implements PaymentFactory {

    @Override
    public PaymentTransaction createTransaction(Order order, Double amount) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setAmount(amount);
        tx.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        tx.setStatus(PaymentStatus.PENDING);
        tx.setCreatedAt(LocalDateTime.now());
        return tx;
    }
}