package com.example.pharmacywebsite.designpattern.DecoratorMore;

import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public interface DiscountApplier {
    double apply(List<OrderItemDto> items, MedicineRepository medicineRepo);
}
