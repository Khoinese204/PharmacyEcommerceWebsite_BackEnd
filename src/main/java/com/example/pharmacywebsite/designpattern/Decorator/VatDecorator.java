package com.example.pharmacywebsite.designpattern.Decorator;

public class VatDecorator extends PriceDecorator {
    private final double vatPercent;

    public VatDecorator(PriceCalculator inner, double vatPercent) {
        super(inner);
        this.vatPercent = vatPercent;
    }

    @Override
    public double calculate() {
        return inner.calculate() * (1 + vatPercent / 100);
    }
}
