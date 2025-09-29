package com.example.pharmacywebsite.designpattern.DecoratorMore;

import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.repository.MedicineRepository;

import java.util.List;

public class ShippingDiscountApplier extends BaseDiscountDecorator {
    private double shippingDiscount = 30_000;

    public ShippingDiscountApplier(DiscountApplier wrapped) {
        super(wrapped);
    }

    @Override
    public double apply(List<OrderItemDto> items, MedicineRepository medicineRepo) {
        double discount = super.apply(items, medicineRepo);
        return discount + shippingDiscount;
    }

    public void extra() {
        System.out.println("Extra logic for shipping discount");
    }
}
