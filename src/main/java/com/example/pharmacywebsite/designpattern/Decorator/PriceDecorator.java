package com.example.pharmacywebsite.designpattern.Decorator;

public abstract class PriceDecorator implements MedicinePriceCalculator {
    protected MedicinePriceCalculator inner;

    public PriceDecorator(MedicinePriceCalculator inner) {
        this.inner = inner;
    }
}