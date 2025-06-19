package com.example.pharmacywebsite.controller;

import com.example.pharmacywebsite.dto.PriceCalculationRequest;
import com.example.pharmacywebsite.dto.PriceCalculationResponse;
import com.example.pharmacywebsite.designpattern.Decorator.MedicinePriceCalculator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    @PostMapping("/calculate")
    public PriceCalculationResponse calculatePrice(@RequestBody PriceCalculationRequest req) {
        double originalPrice = req.getOriginalPrice() != null ? req.getOriginalPrice() : 0.0;
        double discount = req.getDiscountPercent() != null ? req.getDiscountPercent() : 0.0;
        double vat = req.getVatPercent() != null ? req.getVatPercent() : 0.0;

        double finalPrice = MedicinePriceCalculator.calculate(originalPrice, discount, vat);
        return new PriceCalculationResponse(finalPrice);
    }
}
