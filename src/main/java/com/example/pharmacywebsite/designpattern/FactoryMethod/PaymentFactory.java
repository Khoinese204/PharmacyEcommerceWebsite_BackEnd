// factory/PaymentFactory.java
package com.example.pharmacywebsite.designpattern.FactoryMethod;

import com.example.pharmacywebsite.domain.Order;
import com.example.pharmacywebsite.domain.PaymentTransaction;

public interface PaymentFactory {
    PaymentTransaction createTransaction(Order order);
}