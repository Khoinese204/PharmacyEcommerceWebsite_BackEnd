package com.example.pharmacywebsite.designpattern.DecoratorMore;

import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.domain.Medicine;

import java.util.List;

public class CategoryDiscountApplier extends BaseDiscountDecorator {
    private String targetCategory = "Vitamin"; // ví dụ

    private double discountPerItem = 5_000;

    public CategoryDiscountApplier(DiscountApplier wrapped) {
        super(wrapped);
    }

    @Override
    public double apply(List<OrderItemDto> items, MedicineRepository medicineRepo) {
        double discount = super.apply(items, medicineRepo);

        for (OrderItemDto item : items) {
            Medicine medicine = medicineRepo.findById(item.getMedicineId()).orElse(null);
            if (medicine != null && medicine.getCategory() != null
                    && targetCategory.equalsIgnoreCase(medicine.getCategory().getName())) {
                discount += discountPerItem * item.getQuantity();
            }
        }

        return discount;
    }

    public void extra() {
        System.out.println("Extra logic for category discount");
    }
}
