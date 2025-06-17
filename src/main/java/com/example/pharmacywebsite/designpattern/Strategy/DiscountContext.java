package com.example.pharmacywebsite.designpattern.Strategy;

import com.example.pharmacywebsite.dto.CartItemRequest;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public class DiscountContext {
    private DiscountStrategy strategy;
    private MedicineRepository medicineRepo;

    public DiscountContext() {
    }

    public DiscountContext(DiscountStrategy strategy, MedicineRepository medicineRepo) {
        this.strategy = strategy;
        this.medicineRepo = medicineRepo;
    }

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public void setMedicineRepo(MedicineRepository medicineRepo) {
        this.medicineRepo = medicineRepo;
    }

    public double applyDiscount(List<CartItemRequest> items) {
        return strategy.applyDiscount(items, medicineRepo);
    }
}
