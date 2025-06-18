package com.example.pharmacywebsite.designpattern.Decorator;

public class InsuranceDecorator extends PriceDecorator {
    private final double insurancePercent;

    public InsuranceDecorator(MedicinePriceCalculator inner, double insurancePercent) {
        super(inner);
        this.insurancePercent = insurancePercent;
    }

    @Override
    public double calculate() {
        double base = inner.calculate();
        return base * (1 - insurancePercent / 100.0);
    }
}
