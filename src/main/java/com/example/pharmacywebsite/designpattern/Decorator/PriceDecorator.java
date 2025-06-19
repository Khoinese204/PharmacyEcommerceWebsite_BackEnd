package com.example.pharmacywebsite.designpattern.Decorator;

public abstract class PriceDecorator implements PriceCalculator {
    protected final PriceCalculator inner;

    public PriceDecorator(PriceCalculator inner) {
        this.inner = inner;
    }
}
