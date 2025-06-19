package com.example.pharmacywebsite.designpattern.Decorator;

public class DiscountDecorator extends PriceDecorator {
    private final double discountPercent;

    public DiscountDecorator(PriceCalculator inner, double discountPercent) {
        super(inner);
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculate() {
        return inner.calculate() * (1 - discountPercent / 100);
    }
}
