package com.example.pharmacywebsite.designpattern.Strategy;

import com.example.pharmacywebsite.dto.CartItemRequest;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public class CouponCodeFixedAmountStrategy implements DiscountStrategy {

    private final double discountAmount;

    public CouponCodeFixedAmountStrategy(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(List<CartItemRequest> items, MedicineRepository medicineRepo) {
        return discountAmount;
    }
}
