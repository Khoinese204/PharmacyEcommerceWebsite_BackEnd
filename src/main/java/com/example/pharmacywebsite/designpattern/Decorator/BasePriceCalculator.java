package com.example.pharmacywebsite.designpattern.Decorator;

public class BasePriceCalculator implements PriceCalculator {
    private final double basePrice;

    public BasePriceCalculator(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public double calculate() {
        return basePrice;
    }
}
