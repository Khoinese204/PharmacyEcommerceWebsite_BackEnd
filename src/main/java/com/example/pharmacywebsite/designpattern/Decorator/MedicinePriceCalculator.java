package com.example.pharmacywebsite.designpattern.Decorator;

public class MedicinePriceCalculator {

    public static double calculate(double originalPrice, Double discountPercent, Double vatPercent) {
        if (discountPercent == null) discountPercent = 0.0;
        if (vatPercent == null) vatPercent = 0.0;

        PriceCalculator calculator = new BasePriceCalculator(originalPrice);

        if (discountPercent > 0) {
            calculator = new DiscountDecorator(calculator, discountPercent);
        }

        if (vatPercent > 0) {
            calculator = new VatDecorator(calculator, vatPercent);
        }

        return calculator.calculate();
    }
}
