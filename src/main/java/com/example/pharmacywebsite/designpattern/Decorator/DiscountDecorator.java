package com.example.pharmacywebsite.designpattern.Decorator;

public class DiscountDecorator extends PriceDecorator {
    private final double discountPercent;

    public DiscountDecorator(MedicinePriceCalculator inner, double discountPercent) {
        super(inner);
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculate() {
        double base = inner.calculate();
        return base * (1 - discountPercent / 100.0);
    }
}