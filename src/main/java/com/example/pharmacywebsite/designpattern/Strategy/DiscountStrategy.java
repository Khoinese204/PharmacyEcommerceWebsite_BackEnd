package com.example.pharmacywebsite.designpattern.Strategy;

import com.example.pharmacywebsite.dto.CartItemRequest;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public interface DiscountStrategy {
    double applyDiscount(List<CartItemRequest> items, MedicineRepository medicineRepo);
}
