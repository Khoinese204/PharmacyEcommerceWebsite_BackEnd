package com.example.pharmacywebsite.designpattern.FactoryMethod;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;
import com.example.pharmacywebsite.enums.PaymentMethod;
import com.example.pharmacywebsite.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("ZALOPAY")
public class ZalopayPaymentFactory implements PaymentFactory {

    @Override
    public PaymentTransaction createTransaction(Order order, Double amount) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setAmount(amount);
        tx.setPaymentMethod(PaymentMethod.ZALOPAY);
        tx.setStatus(PaymentStatus.PENDING);
        tx.setCreatedAt(LocalDateTime.now());

        // Logic riêng cho ZaloPay (tích hợp sau nếu cần)
        return tx;
    }
}
