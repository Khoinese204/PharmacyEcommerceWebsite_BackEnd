package com.example.pharmacywebsite.designpattern.Decorator;

public class VatDecorator extends PriceDecorator {
    private final double vatPercent;

    public VatDecorator(MedicinePriceCalculator inner, double vatPercent) {
        super(inner);
        this.vatPercent = vatPercent;
    }

    @Override
    public double calculate() {
        double base = inner.calculate();
        return base * (1 + vatPercent / 100.0);
    }
}