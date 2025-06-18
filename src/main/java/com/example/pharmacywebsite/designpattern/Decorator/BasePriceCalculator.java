package com.example.pharmacywebsite.designpattern.Decorator;

import com.example.pharmacywebsite.domain.Medicine;

public class BasePriceCalculator implements MedicinePriceCalculator {
    private final Medicine medicine;

    public BasePriceCalculator(Medicine medicine) {
        this.medicine = medicine;
    }

    @Override
    public double calculate() {
        return medicine.getPrice();
    }
}
