package com.example.pharmacywebsite.designpattern.DecoratorMore;

import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public class BaseDiscountApplier implements DiscountApplier {
    @Override
    public double apply(List<OrderItemDto> items, MedicineRepository medicineRepo) {
        return 0.0; // Không giảm giá gốc
    }
}
