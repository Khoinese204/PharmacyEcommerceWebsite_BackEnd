package com.example.pharmacywebsite.designpattern.Strategy;

import com.example.pharmacywebsite.dto.CartItemRequest;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.domain.Medicine;

import java.util.List;

public class ThresholdFixedAmountDiscountStrategy implements DiscountStrategy {

    private final double threshold;
    private final double discountAmount;

    public ThresholdFixedAmountDiscountStrategy(double threshold, double discountAmount) {
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(List<CartItemRequest> items, MedicineRepository medicineRepo) {
        double total = items.stream()
                .map(item -> medicineRepo.findById(item.getId()).orElse(null))
                .filter(m -> m != null)
                .mapToDouble(m -> {
                    int qty = items.stream()
                            .filter(i -> i.getId().equals(m.getId()))
                            .findFirst()
                            .map(CartItemRequest::getQuantity)
                            .orElse(0);
                    return m.getOriginalPrice() * qty;
                })
                .sum();

        return total >= threshold ? discountAmount : 0;
    }
}
