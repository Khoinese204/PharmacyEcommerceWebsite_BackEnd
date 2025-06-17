package com.example.pharmacywebsite.designpattern.Strategy;

import com.example.pharmacywebsite.dto.CartItemRequest;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.domain.Medicine;

import java.util.List;

public class QuantityBasedCategoryDiscountStrategy implements DiscountStrategy {

    private final Integer categoryId;
    private final int minQuantity;
    private final double discountAmount;

    public QuantityBasedCategoryDiscountStrategy(Integer categoryId, int minQuantity, double discountAmount) {
        this.categoryId = categoryId;
        this.minQuantity = minQuantity;
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(List<CartItemRequest> items, MedicineRepository medicineRepo) {
        int totalQty = items.stream()
                .map(item -> medicineRepo.findById(item.getId()).orElse(null))
                .filter(m -> m != null && m.getCategory().getId().equals(categoryId))
                .mapToInt(m -> items.stream()
                        .filter(i -> i.getId().equals(m.getId()))
                        .findFirst()
                        .map(CartItemRequest::getQuantity)
                        .orElse(0))
                .sum();

        return totalQty >= minQuantity ? discountAmount : 0;
    }
}
