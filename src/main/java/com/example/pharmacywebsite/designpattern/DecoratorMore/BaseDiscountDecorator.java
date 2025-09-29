package com.example.pharmacywebsite.designpattern.DecoratorMore;

import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public abstract class BaseDiscountDecorator implements DiscountApplier {
    protected DiscountApplier wrapped;

    public BaseDiscountDecorator(DiscountApplier wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public double apply(List<OrderItemDto> items, MedicineRepository medicineRepo) {
        return wrapped.apply(items, medicineRepo);
    }
}
