package com.example.pharmacywebsite.designpattern.FactoryMethod;

import com.example.pharmacywebsite.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentFactoryProvider {

    private final Map<String, PaymentFactory> factoryMap;

    public PaymentFactory getFactory(PaymentMethod method) {
        PaymentFactory factory = factoryMap.get(method.name());
        if (factory == null) {
            throw new IllegalArgumentException("Không hỗ trợ phương thức thanh toán: " + method);
        }
        return factory;
    }
}
