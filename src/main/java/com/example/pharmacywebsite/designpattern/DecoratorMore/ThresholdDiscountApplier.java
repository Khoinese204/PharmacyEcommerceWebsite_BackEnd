package com.example.pharmacywebsite.designpattern.DecoratorMore;

import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public class ThresholdDiscountApplier extends BaseDiscountDecorator {
    private double threshold = 500_000;
    private double discountAmount = 50_000;

    public ThresholdDiscountApplier(DiscountApplier wrapped) {
        super(wrapped);
    }

    @Override
    public double apply(List<OrderItemDto> items, MedicineRepository medicineRepo) {
        double discount = super.apply(items, medicineRepo);

        double total = items.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        if (total >= threshold) {
            discount += discountAmount;
        }

        return discount;
    }

    public void extra() {
        System.out.println("Extra logic for threshold discount");
    }
}
