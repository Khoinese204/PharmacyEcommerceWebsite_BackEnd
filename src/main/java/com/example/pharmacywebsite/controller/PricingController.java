package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.FinalPriceCalculationRequest;
import com.example.pharmacywebsite.dto.FinalPriceCalculationResponse;
import com.example.pharmacywebsite.dto.OrderItemDto;
import com.example.pharmacywebsite.dto.PriceCalculationRequest;
import com.example.pharmacywebsite.dto.PriceCalculationResponse;
import com.example.pharmacywebsite.repository.MedicineRepository;
import com.example.pharmacywebsite.designpattern.Decorator.MedicinePriceCalculator;
import com.example.pharmacywebsite.designpattern.DecoratorMore.BaseDiscountApplier;
import com.example.pharmacywebsite.designpattern.DecoratorMore.CategoryDiscountApplier;
import com.example.pharmacywebsite.designpattern.DecoratorMore.DiscountApplier;
import com.example.pharmacywebsite.designpattern.DecoratorMore.ShippingDiscountApplier;
import com.example.pharmacywebsite.designpattern.DecoratorMore.ThresholdDiscountApplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    @Autowired
    private MedicineRepository medicineRepository;

    @PostMapping("/calculate")
    public PriceCalculationResponse calculatePrice(@RequestBody PriceCalculationRequest req) {
        double originalPrice = req.getOriginalPrice() != null ? req.getOriginalPrice() : 0.0;
        double discount = req.getDiscountPercent() != null ? req.getDiscountPercent() : 0.0;
        double vat = req.getVatPercent() != null ? req.getVatPercent() : 0.0;

        double finalPrice = MedicinePriceCalculator.calculate(originalPrice, discount, vat);
        return new PriceCalculationResponse(finalPrice);
    }

    @PostMapping("/calculateFinalPrice")
    public FinalPriceCalculationResponse calculateFinalPrice(@RequestBody FinalPriceCalculationRequest req) {
        List<OrderItemDto> items = req.getItems();

        DiscountApplier discountApplier = new ThresholdDiscountApplier(
                new ShippingDiscountApplier(
                        new CategoryDiscountApplier(
                                new BaseDiscountApplier())));

        double discount = discountApplier.apply(items, medicineRepository);

        double originalTotal = items.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        double finalPrice = originalTotal - discount;
        if (finalPrice < 0)
            finalPrice = 0;

        return new FinalPriceCalculationResponse(finalPrice);
    }
}
