package com.example.pharmacywebsite.designpattern.Strategy;

import com.example.pharmacywebsite.dto.CartItemRequest;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public class ShippingDiscountStrategy implements DiscountStrategy {

    private final double thresholdAmount;
    private final double shippingDiscount;

    public ShippingDiscountStrategy(double thresholdAmount, double shippingDiscount) {
        this.thresholdAmount = thresholdAmount;
        this.shippingDiscount = shippingDiscount;
    }

    @Override
    public double applyDiscount(List<CartItemRequest> items, MedicineRepository medicineRepo) {
        double total = items.stream()
                .mapToDouble(i -> medicineRepo.findById(i.getId())
                        .map(m -> m.getOriginalPrice() * i.getQuantity())
                        .orElse(0.0))
                .sum();

        return total >= thresholdAmount ? shippingDiscount : 0.0;
    }

}
